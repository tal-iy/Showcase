import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

public class TftpClient implements Runnable {

    public static final byte[] OP_RRQ = {(byte)0, (byte)1};
    public static final byte[] OP_WRQ = {(byte)0, (byte)2};
    public static final byte[] OP_DATA = {(byte)0, (byte)3};
    public static final byte[] OP_ACK = {(byte)0, (byte)4};
    public static final byte[] OP_ERROR = {(byte)0, (byte)5};
    public static final byte[] OP_OACK = {(byte)0, (byte)6};
    private static int WINDOW_SIZE = 32;

    private TftpSender msg;

    private static boolean dropping = false;
    private static char operation = 'n';
    private static int ipVersion = 0;
    private static String host = "127.0.0.1";
    private static int port = 2760;
    private static String fileName = "test.png";

    public static void main(String args[]) throws Exception {
        boolean gotIp = false;
        boolean gotPort = false;
        boolean gotFile = false;
        boolean gotWindow = false;

        for (String str:args) {
            if (gotIp) {
                host = str;
                gotIp = false;
            } else if (gotPort) {
                port = Integer.parseInt(str);
                gotPort = false;
            } else if (gotWindow) {
                WINDOW_SIZE = Integer.parseInt(str);
                gotWindow = false;
            } else if (str.equals("-d"))
                dropping = true;
            else if (str.equals("-w"))
                operation = 'w';
            else if (str.equals("-r"))
                operation = 'r';
            else if (str.equals("-ipv4"))
                ipVersion = 4;
            else if (str.equals("-ipv6"))
                ipVersion = 6;
            else if (str.equals("-host"))
                gotIp = true;
            else if (str.equals("-port"))
                gotPort = true;
            else if (str.equals("-window"))
                gotWindow = true;
            else if (!gotFile) {
                fileName = str;
                gotFile = true;
            } else {
                System.out.println("Invalid argument: "+str);
            }
        }

        if (ipVersion == 4)
            System.setProperty("java.net.preferIPv4Addresses", "true");
        else if (ipVersion == 6)
            System.setProperty("java.net.preferIPv6Addresses", "true");

        new Thread(new TftpClient()).start();
    }

    public TftpClient() throws Exception {
        msg = new TftpSender(new DatagramSocket(port), host, port, dropping);
    }

    @Override
    public void run() {
        try {
            // Generate cipher key
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
            SecretKey cipherKey = keygen.generateKey();
            String cipherKeyString = Base64.getEncoder().encodeToString(cipherKey.getEncoded());

            if (operation == 'w') {

                // Build WRQ message
                msg.startMessage(1024);
                msg.put(OP_WRQ);
                msg.put(fileName.getBytes(StandardCharsets.UTF_8));
                msg.put((byte) 0);
                msg.put("netascii".getBytes(StandardCharsets.UTF_8));
                msg.put((byte) 0);
                msg.put("key".getBytes(StandardCharsets.UTF_8));
                msg.put((byte) 0);
                msg.put(cipherKeyString.getBytes(StandardCharsets.UTF_8));
                msg.put((byte) 0);
                msg.put("window".getBytes(StandardCharsets.UTF_8));
                msg.put((byte) 0);
                msg.put((new Integer(WINDOW_SIZE)).toString().getBytes(StandardCharsets.UTF_8));
                msg.put((byte) 0);

                // Send message
                //System.out.println("-> WRQ: "+fileName);
                msg.flushAndWait();

                while(!msg.isOp(OP_OACK))
                    msg.receive();

                //System.out.println("<- OACK");

                try {
                    File file = new File("./client/" + fileName);
                    FileInputStream in = new FileInputStream(file);

                    long throughputStart = System.currentTimeMillis();
                    long dataSize = 0;

                    int block = 1;
                    TftpWindow window = new TftpWindow(WINDOW_SIZE);
                    msg.setTimeout(window.RTO);
                    boolean sending = true;
                    boolean eof = false;
                    while (sending) {

                        // Push as much as possible into window
                        while(!window.full()) {

                            // Build DATA message
                            msg.startMessage(516);
                            msg.put(OP_DATA);
                            msg.put((short)block);

                            // Read at most 512 bytes
                            int i = 0;
                            int c = -1;
                            byte[] data = new byte[512];
                            for (; i < 512 && !(eof = ((c = in.read()) == -1)); i++)
                                data[i] = (byte)c;

                            msg.put(xorEncrypt(data, cipherKey.getEncoded(), i));
                            dataSize += i;

                            // Send message and push it into window if there is data
                            if (msg.msg.position() >= 5) {
                                //System.out.println("-> DATA: " + block);
                                window.push(msg.flushTemp(), block);
                                block ++;
                            } else break;
                        }

                        // Start RTO timer
                        window.startRtt(block);

                        // Make sure there was an ACK with a valid block
                        msg.receive();
                        while (!msg.isOp(OP_ACK) || (msg.isOp(OP_ACK) && msg.getBlock() < window.firstId()))
                            msg.receive();

                        // Calculate RTT and update RTO
                        window.endRtt(msg.getBlock());
                        msg.setTimeout(window.RTO);

                        //if (msg.getBlock() == block)
                            //System.out.println("<- ACK: "+msg.getBlock());
                        //else
                            //System.out.println("<- missing ACK: "+msg.getBlock());

                        // Validate all packets with block less than ACK
                        window.validate(msg.getBlock());

                        // Stop sending if EOF and all validated
                        if (eof && msg.getBlock() == block) {
                            sending = false;
                        } else if (window.get(msg.getBlock()) != null) {

                            // Resend packet with ACK block
                            //System.out.println("-> Re-DATA: " + msg.getBlock());
                            msg.flush(window.get(msg.getBlock()));
                        }
                    }

                    in.close();

                    long timeDelta = System.currentTimeMillis()-throughputStart;
                    long throughput = dataSize/(timeDelta);
                    System.out.println("Transaction complete! File: "+dataSize+" bytes, Time: "+timeDelta+" ms, Throughput: "+throughput+" bytes/millisecond");

                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            } else if (operation == 'r') {

                // Build RRQ message
                msg.startMessage(1024);
                msg.put(OP_RRQ);
                msg.put(fileName.getBytes(StandardCharsets.UTF_8));
                msg.put((byte) 0);
                msg.put("netascii".getBytes(StandardCharsets.UTF_8));
                msg.put((byte) 0);
                msg.put("key".getBytes(StandardCharsets.UTF_8));
                msg.put((byte) 0);
                msg.put(cipherKeyString.getBytes(StandardCharsets.UTF_8));
                msg.put((byte) 0);
                msg.put("window".getBytes(StandardCharsets.UTF_8));
                msg.put((byte) 0);
                msg.put((new Integer(WINDOW_SIZE)).toString().getBytes(StandardCharsets.UTF_8));
                msg.put((byte) 0);

                // Send message
                //System.out.println("-> RRQ: "+fileName);
                msg.flushAndWait();

                while(!msg.isOp(OP_OACK))
                    msg.receive();

                // Check if OACK has valid key
                HashMap<String, String> options = msg.getOackOptions();
                if (!options.getOrDefault("key", " ").equals(cipherKeyString)) {
                    System.out.println("Recieved OACK with no or wrong key!");
                    System.out.println(cipherKeyString);
                    System.exit(1);
                }

                // Send ACK
                msg.startMessage(4);
                msg.put(OP_ACK);
                msg.put((short) 0);
                //System.out.println("-> ACK: 0");
                msg.flushAndWait();

                while(!msg.isOp(OP_DATA))
                    msg.receive();

                HashMap<Integer, DatagramPacket> window = new HashMap<>();
                TftpWindow timer = new TftpWindow(WINDOW_SIZE);
                msg.setTimeout(timer.RTO);
                window.put(msg.getBlock(), msg.packetR);
                //System.out.println("<- DATA: "+msg.getBlock());

                File file = new File("./client/"+fileName);
                file.createNewFile();
                OutputStream out = new FileOutputStream(file);

                long throughputStart = System.currentTimeMillis();
                long dataSize = 0;

                int block = 1;
                boolean sending = true;
                while(sending) {

                    // Start RTO timer
                    timer.startRtt(block);

                    // Keep receiving data until timeout or full window
                    boolean timedOut = false;
                    long startTime = System.currentTimeMillis();
                    while(window.size() < WINDOW_SIZE && System.currentTimeMillis()-startTime < timer.RTO) {
                        DatagramPacket packet = msg.receive();

                        // Set timeout flag for sending ACK
                        if (packet == null) {
                            timedOut = true;
                            break;
                        } else if (msg.isOp(OP_DATA) && !window.containsKey(msg.getBlock()) && msg.getBlock() >= block && msg.getBlock() < block+WINDOW_SIZE) {
                            window.put(msg.getBlock(), packet);
                            //System.out.println("<- DATA: "+msg.getBlock());

                            // Calculate RTT and update RTO
                            timer.endRtt(msg.getBlock());
                            msg.setTimeout(timer.RTO);

                            startTime = System.currentTimeMillis();

                            if (msg.getData().length < 512)
                                break;
                        }
                    }

                    // Write all consecutive blocks in the window
                    DatagramPacket packet;
                    while((packet = window.get(block)) != null) {
                        window.remove(block);
                        byte[] data = xorEncrypt(msg.getData(packet),cipherKey.getEncoded(),msg.getData(packet).length);

                        //System.out.println("Writing DATA: " + block + ", len: " + data.length);
                        out.write(data);

                        dataSize += data.length;

                        if (data.length < 512)
                            sending = false;

                        block++;
                    }

                    // Check for timeout or empty window
                    if (timedOut || window.isEmpty()) {

                        // Send ACK for next expected block
                        msg.startMessage(4);
                        msg.put(OP_ACK);
                        msg.put((short) block);
                        //System.out.println("-> ACK: " + block);
                        msg.flush();
                    }
                }

                out.close();

                long timeDelta = System.currentTimeMillis()-throughputStart;
                long throughput = dataSize/(timeDelta);
                System.out.println("Transaction complete! File: "+dataSize+" bytes, Time: "+timeDelta+" ms, Throughput: "+throughput+" bytes/millisecond");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            msg.close();
        }
    }

    public byte[] xorEncrypt(byte[] message, byte[] key, int size) {
        byte[] result = new byte[size];

        for(int i=0; i<size; i++)
            if (i < message.length)
                result[i] = (byte) (message[i] ^ key[i % key.length]);
            else
                result[i] = ' ';

        return result;
    }
}
