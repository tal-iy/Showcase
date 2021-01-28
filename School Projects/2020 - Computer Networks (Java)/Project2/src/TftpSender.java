import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

public class TftpSender {

    public DatagramSocket socket;
    public DatagramPacket packetR;
    public DatagramPacket packetS;
    public InetAddress host;
    public int port;
    public boolean dropping;

    public ByteBuffer msg;

    public TftpSender(DatagramSocket socket, String host, int port, boolean dropping) throws IOException {
        this.socket = socket;
        this.host = InetAddress.getByName(host);
        this.port = port;
        this.dropping = dropping;

        socket.setSoTimeout(100);
    }

    public TftpSender(DatagramSocket socket, int port, boolean dropping) throws IOException {
        this.socket = socket;
        this.host = null;
        this.port = port;
        this.dropping = dropping;

        socket.setSoTimeout(100);
    }

    public void close() {
        socket.close();
    }

    public void setTimeout(int timeout) {
        try {
            socket.setSoTimeout(timeout);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startMessage(int size) {
        msg = ByteBuffer.allocate(size);
    }

    public void put(byte data) {
        msg.put(data);
    }

    public void put(byte[] data) {
        msg.put(data);
    }

    public void put(short data) {
        put(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(data).array());
    }

    public DatagramPacket receive() {
        byte[] buf = new byte[1024];
        packetR = new DatagramPacket(buf, buf.length);

        try {
            // Simulate dropping packets
            if (!dropping || Math.random() >= 0.01)
                socket.receive(packetR);
            else {
                socket.receive(new DatagramPacket(new byte[1024], 1024));
                //System.out.println("Dropping packet!");
            }

        } catch (SocketTimeoutException ex) {
            // Signal timeout
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        return packetR;
    }

    // Flush current data and return packet
    public DatagramPacket flushTemp() {
        DatagramPacket ret = null;

        try {
            // Convert buffer to byte array
            int size = msg.position();
            byte[] out = new byte[size];
            msg.position(0);
            msg.get(out);
            msg.position(size);

            if (host == null) {
                if (packetR != null) {
                    host = packetR.getAddress();
                    port = packetR.getPort();
                } else {
                    return null;
                }
            }

            ret = new DatagramPacket(out, out.length, host, port);

            // Send packet
            socket.send(ret);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    // Flush a packet
    public void flush(DatagramPacket packet) {
        try {
            // Send packet
            socket.send(packet);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Flush current data using global packet
    public void flush() {
        try {
            // Convert buffer to byte array
            int size = msg.position();
            byte[] out = new byte[size];
            msg.position(0);
            msg.get(out);
            msg.position(size);

            if (host == null) {
                if (packetR != null) {
                    host = packetR.getAddress();
                    port = packetR.getPort();
                } else {
                    return;
                }
            }

            packetS = new DatagramPacket(out, out.length, host, port);

            // Send packet
            socket.send(packetS);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void flushAndWait() {
        try {
            // Convert buffer to byte array
            int size = msg.position();
            byte[] out = new byte[size];
            msg.position(0);
            msg.get(out);
            msg.position(size);

            // Send packet and wait for ack, try again if timeout
            boolean sending = true;
            while (sending) {

                if (host == null) {
                    if (packetR != null) {
                        host = packetR.getAddress();
                        port = packetR.getPort();
                    } else {
                        return;
                    }
                }

                packetS = new DatagramPacket(out, out.length, host, port);

                socket.send(packetS);

                byte[] buf = new byte[1024];
                packetR = new DatagramPacket(buf, buf.length);

                try {
                    // Simulate dropping packets
                    if (!dropping || Math.random() >= 0.01)
                        socket.receive(packetR);
                    else {
                        socket.receive(new DatagramPacket(new byte[1024], 1024));
                        //System.out.println("Dropping packet!");
                    }

                    sending = false;
                } catch (SocketTimeoutException ex) { } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public byte[] getOpcode() {
        return Arrays.copyOfRange(packetR.getData(), 0, 2);
    }

    public byte[] getOpcode(DatagramPacket packet) {
        return Arrays.copyOfRange(packet.getData(), 0, 2);
    }

    public boolean isOp(byte[] opCode) {
        return Arrays.equals(opCode, getOpcode());
    }

    public boolean isOp(byte[] opCode, DatagramPacket packet) {
        return Arrays.equals(opCode, getOpcode(packet));
    }

    public int getBlock() {
        byte[] bytes = Arrays.copyOfRange(packetR.getData(), 2, 4);
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public int getBlock(DatagramPacket packet) {
        byte[] bytes = Arrays.copyOfRange(packet.getData(), 2, 4);
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public String getFile() {
        byte[] data = packetR.getData();

        // Find end of string
        int strLen = 2;
        for (; strLen<data.length; strLen++)
            if (data[strLen] == (byte)0)
                break;

        return new String(Arrays.copyOfRange(data, 2, strLen), StandardCharsets.UTF_8);
    }

    public byte[] getData() {
        return Arrays.copyOfRange(packetR.getData(), 4, packetR.getLength());
    }

    public byte[] getData(DatagramPacket packet) {
        return Arrays.copyOfRange(packet.getData(), 4, packet.getLength());
    }

    public HashMap<String, String> getOptions() {
        byte[] data = packetR.getData();
        int len = packetR.getLength();

        int start;
        int end = 2;

        // Skip filename
        for (start = end; end<len; end++)
            if (data[end] == (byte)0)
                break;
        String fileName = new String(Arrays.copyOfRange(data, start, end), StandardCharsets.UTF_8);

        // Skip mode
        end++;
        for (start = end; end<len; end++)
            if (data[end] == (byte)0)
                break;
        String mode = new String(Arrays.copyOfRange(data, start, end), StandardCharsets.UTF_8);


        // Build hashmap of options
        HashMap<String, String> map = new HashMap<>();
        while (end<len) {
            // Get option name
            end++;
            for (start = end; end<len; end++)
                if (data[end] == (byte)0)
                    break;

            if (start == end)
                break;
            String name = new String(Arrays.copyOfRange(data, start, end), StandardCharsets.UTF_8);

            // Get option value
            end++;
            for (start = end; end<len; end++)
                if (data[end] == (byte)0)
                    break;
            String value = new String(Arrays.copyOfRange(data, start, end), StandardCharsets.UTF_8);

            // Put option into map
            map.put(name, value);
        }

        return map;
    }

    public HashMap<String, String> getOackOptions() {
        byte[] data = packetR.getData();
        int len = packetR.getLength();

        int start;
        int end = 1;

        // Build hashmap of options
        HashMap<String, String> map = new HashMap<>();
        while (end<len) {
            // Get option name
            end++;
            for (start = end; end<len; end++)
                if (data[end] == (byte)0)
                    break;

            if (start == end)
                break;
            String name = new String(Arrays.copyOfRange(data, start, end), StandardCharsets.UTF_8);

            // Get option value
            end++;
            for (start = end; end<len; end++)
                if (data[end] == (byte)0)
                    break;
            String value = new String(Arrays.copyOfRange(data, start, end), StandardCharsets.UTF_8);

            // Put option into map
            map.put(name, value);
        }

        return map;
    }
}
