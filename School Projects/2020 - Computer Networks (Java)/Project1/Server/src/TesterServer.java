import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TesterServer {

    private final int LATENCY_TCP = 0;
    private final int LATENCY_UDP = 1;
    private final int THROUGHPUT_TCP = 2;
    private final int INTERACTION_TCP = 3;
    private final int INTERACTION_UDP = 4;

    private final int PORT_UDP = 2760; // 2760 to 2769
    private final int PORT_TCP = 2761; // 2760 to 2769
    private final int TRIALS = 30;

    private int experiment = -1;

    private DatagramSocket socketUDP;
    private ServerSocket socketTCP;

    public TesterServer() {
        try {
            // Figure out the public IP of this server
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            String ip = new BufferedReader(new InputStreamReader(whatismyip.openStream())).readLine();
            System.out.println("Server: Starting at ip " + ip);
        } catch (IOException ex) {
            System.out.println("Server: Error: Couldn't find own IP!");
            System.exit(1);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (;;) {
            try {
                System.out.println("Server: Enter experiment number (0-4) (5:close): ");
                experiment = Integer.parseInt(reader.readLine());
            } catch (IOException ex) {
                System.out.println("Server: Error: Invalid input!");
                System.exit(1);
            }

            // Repeat experiment
            for (int i = 0; i < TRIALS; i++) {

                // Do different experiments based on input
                if (experiment == LATENCY_TCP) {
                    System.out.println("Server: Starting TCP latency experiment!");
                    testLatencyTCP(8);
                    testLatencyTCP(64);
                    testLatencyTCP(1024);
                } else if (experiment == LATENCY_UDP) {
                    System.out.println("Server: Starting UDP latency experiment!");
                    testLatencyUDP(8);
                    testLatencyUDP(64);
                    testLatencyUDP(1024);
                } else if (experiment == THROUGHPUT_TCP) {
                    System.out.println("Server: Starting TCP throughput experiment!");
                    testThroughputTCP(1024);
                    testThroughputTCP(16 * 1024);
                    testThroughputTCP(64 * 1024);
                    testThroughputTCP(256 * 1024);
                    testThroughputTCP(1024 * 1024);
                } else if (experiment == INTERACTION_TCP) {
                    System.out.println("Server: Starting TCP interaction experiment!");
                    testInteractionTCP(1024, 1024);
                    testInteractionTCP(2048, 512);
                    testInteractionTCP(4096, 256);
                } else if (experiment == INTERACTION_UDP) {
                    System.out.println("Server: Starting UDP interaction experiment!");
                    testInteractionUDP(1024, 1024);
                    testInteractionUDP(2048, 512);
                    testInteractionUDP(4096, 256);
                } else if (experiment == 5) {
                    System.out.println("Server: Closing");
                    System.exit(-1);
                } else {
                    System.out.println("Server: Error: Unknown experiment!");
                    System.exit(1);
                }
            }
        }
    }

    public void testLatencyTCP(int size) {
        try {
            // Connect to a client
            socketTCP = new ServerSocket(PORT_TCP);
            Socket client = socketTCP.accept();
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("Server: Connected TCP...");

            // Read incoming data
            byte[] msg = new byte[size];
            in.readFully(msg, 0, size);

            // Relay data back to the client
            out.write(msg);
            out.flush();
            System.out.println("Server: Message : " + new String(xorEncrypt(msg, "CSCFourFourFive".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));

            // Close the connection
            out.close();
            in.close();
            client.close();
            socketTCP.close();
            System.out.println("Server: Disconnected TCP...");

        } catch (IOException ex) {
            System.out.println("Server: Error: Unable to communicate with client!");
            System.exit(1);
        }
    }

    public void testLatencyUDP(int size) {
        try {
            // Connect to a client
            socketUDP = new DatagramSocket(PORT_UDP);
            System.out.println("Server: Connected UDP...");

            // Read incoming data
            byte[] msg = new byte[size];
            DatagramPacket packet = new DatagramPacket(msg, size);
            receiveWithTimeout(packet, 2000, 10);

            // Relay data back to the client
            packet = new DatagramPacket(msg, msg.length, packet.getAddress(), PORT_UDP);
            sendWithTimeout(packet, 2000, 10);
            System.out.println("Server: Message : " + new String(xorEncrypt(packet.getData(), "CSCFourFourFive".getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));

            // Close the connection
            socketUDP.close();
            System.out.println("Server: Disconnected UDP...");

        } catch (SocketException ex) {
            System.out.println("Server: Error: Unable to communicate with client!");
            System.exit(1);
        }
    }

    public void testThroughputTCP(int size) {
        try {
            // Connect to a client
            socketTCP = new ServerSocket(PORT_TCP);
            Socket client = socketTCP.accept();
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("Server: Connected TCP...");

            // Loop reading the data until timeout
            int messages = 0;
            for (; ; ) {
                try {
                    // Read incoming data
                    byte[] msg = new byte[size];
                    in.readFully(msg, 0, size);

                    if (msg[0] == 'C' && msg[1] == 'L' && msg[2] == 'O' && msg[3] == 'S' && msg[4] == 'E') {
                        System.out.println("Server: CLOSE message");
                        break;
                    } else {
                        // Relay data back to the client
                        out.write(msg);
                        out.flush();
                        messages++;
                    }
                } catch (EOFException ex) { }
            }

            System.out.println("Server: Received " + messages + " messages!");

            // Close the connection
            out.close();
            in.close();
            client.close();
            socketTCP.close();
            System.out.println("Server: Disconnected TCP...");

        } catch (IOException ex) {
            System.out.println("Server: Error: Unable to communicate with client!");
            System.exit(1);
        }
    }

    public void testInteractionTCP(int numMessages, int size)  {
        try {
            // Connect to a client
            socketTCP = new ServerSocket(PORT_TCP);
            Socket client = socketTCP.accept();
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("Server: Connected TCP...");

            try {
                // Read incoming data
                for (int i = 0; i < numMessages; i++) {
                    byte[] msg = new byte[size];
                    in.readFully(msg, 0, size);

                    // Send back acknowledgment
                    byte[] res = {'F', 'I', 'N', 'I', 'S', 'H', 'E', 'D'};
                    out.write(res);
                    out.flush();
                }
            } catch (EOFException ex) { }

            // Close the connection
            out.close();
            in.close();
            client.close();
            socketTCP.close();
            System.out.println("Server: Disconnected TCP...");

        } catch (IOException ex) {
            System.out.println("Server: Error: Unable to communicate with client!");
            System.exit(1);
        }
    }

    public void testInteractionUDP(int numMessages, int size)  {
        try {
            // Connect to a client
            socketUDP = new DatagramSocket(PORT_UDP);
            System.out.println("Server: Connected UDP...");

            // Read incoming data
            DatagramPacket packet = null;
            for (int i = 0; i < numMessages; i++) {
                byte[] msg = new byte[size];
                packet = new DatagramPacket(msg, size);
                receiveWithTimeout(packet, 2000, 1);

                if (packet.getPort() != -1) {
                    byte[] res = {'F', 'I', 'N', 'I', 'S', 'H', 'E', 'D'};
                    packet = new DatagramPacket(res, res.length, packet.getAddress(), packet.getPort());
                    sendWithTimeout(packet, 2000, 1);
                }
            }

            // Close the connection
            socketUDP.close();
            System.out.println("Server: Disconnected UDP...");
        } catch (SocketException ex) {
            System.out.println("Server: Error: Unable to communicate with client!");
            System.exit(1);
        }
    }

    public void sendWithTimeout(DatagramPacket packet, int timeout, int times) {
        try {
            socketUDP.setSoTimeout(timeout);
        } catch (SocketException ex) {
            System.out.println("Server: Error: Unable to set message timeout!");
            System.exit(1);
        }

        // Send the message repeatedly until one succeeds before timeout
        boolean sending = true;
        int life = 0;
        while(sending && life < times) {
            try {
                socketUDP.send(packet);
                sending = false;
            } catch (SocketTimeoutException ex) {
                life++;
            } catch (IOException ex) {
                ex.printStackTrace();
                sending = false;
            }
        }
    }

    public void receiveWithTimeout(DatagramPacket packet, int timeout, int times) {
        try {
            socketUDP.setSoTimeout(timeout);
        } catch (SocketException ex) {
            System.out.println("Server: Error: Unable to set message timeout!");
            System.exit(1);
        }

        // Receive the message repeatedly until one succeeds before timeout
        boolean receiving = true;
        int life = 0;
        while(receiving && life < times) {
            try {
                socketUDP.receive(packet);
                receiving = false;
            } catch (SocketTimeoutException ex) {
                life++;
            } catch (IOException ex) {
                ex.printStackTrace();
                receiving = false;
            }
        }
    }

    public byte[] xorEncrypt(byte[] message, byte[] key) {
        byte[] result = new byte[message.length];

        for(int i=0; i<message.length; i++)
            result[i] = (byte) (message[i] ^ key[i % key.length]);

        return result;
    }

    public static void main(String[] args) {
        new TesterServer();
    }
}
