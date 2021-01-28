import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TesterClient {

    private final int LATENCY_TCP = 0;
    private final int LATENCY_UDP = 1;
    private final int THROUGHPUT_TCP = 2;
    private final int INTERACTION_TCP = 3;
    private final int INTERACTION_UDP = 4;

    private final int PORT_UDP = 2760; // 2760 to 2769
    private final int PORT_TCP = 2761; // 2760 to 2769
    private final int TRIALS = 30;

    private String host = "127.0.0.1"; // Localhost as default
    private int experiment = -1;

    private DatagramSocket socketUDP;
    private Socket socketTCP;

    public TesterClient() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Client: Enter host: ");
            host = reader.readLine();
            System.out.println("Client: Enter experiment number: ");
            experiment = Integer.parseInt(reader.readLine());

            long[][] latencyTCP = new long[3][TRIALS];
            long[][] latencyUDP = new long[3][TRIALS];

            int[][] throughputTCP = new int[5][TRIALS];

            long[][] interactionTCP = new long[3][TRIALS];
            long[][] interactionUDP = new long[3][TRIALS];

            (new File("data.txt")).createNewFile();
            FileWriter output = new FileWriter("data.txt");

            if (experiment == LATENCY_TCP) {
                // Do TCP latency experiment
                for (int i = 0; i < TRIALS; i++) {
                    System.out.println("\nClient: TCP Latency (8 bytes): " + (latencyTCP[0][i] = testLatencyTCP(8)));
                    Thread.sleep(500);
                    System.out.println("Client: TCP Latency (64 bytes): " + (latencyTCP[1][i] = testLatencyTCP(64)));
                    Thread.sleep(500);
                    System.out.println("Client: TCP Latency (1024 bytes): " + (latencyTCP[2][i] = testLatencyTCP(1024)));
                    Thread.sleep(1000);
                }

                // Save results to file and compute averages
                output.write("TCP Latency\n");
                double[] avgLatencyTCP = new double[3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < TRIALS; j++) {
                        output.write(latencyTCP[i][j] + "\n");
                        avgLatencyTCP[i] += latencyTCP[i][j];
                    }
                    avgLatencyTCP[i] /= TRIALS;
                    output.write("\n");
                }

                // Print averages
                System.out.println("Client: Average TCP Latency (8 bytes): " + avgLatencyTCP[0]);
                System.out.println("Client: Average TCP Latency (64 bytes): " + avgLatencyTCP[1]);
                System.out.println("Client: Average TCP Latency (1024 bytes): " + avgLatencyTCP[2]);

            } else if (experiment == LATENCY_UDP) {
                // Do UDP latency experiment
                for (int i = 0; i < TRIALS; i++) {
                    System.out.println("\nClient: UDP Latency (8 bytes): " + (latencyUDP[0][i] = testLatencyUDP(8)));
                    Thread.sleep(500);
                    System.out.println("Client: UDP Latency (64 bytes): " + (latencyUDP[1][i] = testLatencyUDP(64)));
                    Thread.sleep(500);
                    System.out.println("Client: UDP Latency (1024 bytes): " + (latencyUDP[2][i] = testLatencyUDP(1024)));
                    Thread.sleep(1000);
                }

                // Save results to file and compute averages
                output.write("UDP Latency\n");
                double[] avgLatencyUDP = new double[3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < TRIALS; j++) {
                        output.write(latencyUDP[i][j] + "\n");
                        avgLatencyUDP[i] += latencyUDP[i][j];
                    }
                    avgLatencyUDP[i] /= TRIALS;
                    output.write("\n");
                }

                // Print averages
                System.out.println("Client: Average UDP Latency (8 bytes): " + avgLatencyUDP[0]);
                System.out.println("Client: Average UDP Latency (64 bytes): " + avgLatencyUDP[1]);
                System.out.println("Client: Average UDP Latency (1024 bytes): " + avgLatencyUDP[2]);

            } else if (experiment == THROUGHPUT_TCP) {
                // Do TCP throughput experiment
                for (int i = 0; i < TRIALS; i++) {
                    System.out.println("\nClient: TCP Throughput (1K bytes): " + (throughputTCP[0][i] = testThroughputTCP(1024)));
                    Thread.sleep(500);
                    System.out.println("Client: TCP Throughput (16K bytes): " + (throughputTCP[1][i] = testThroughputTCP(16 * 1024)));
                    Thread.sleep(500);
                    System.out.println("Client: TCP Throughput (64K bytes): " + (throughputTCP[2][i] = testThroughputTCP(64 * 1024)));
                    Thread.sleep(500);
                    System.out.println("Client: TCP Throughput (256K bytes): " + (throughputTCP[3][i] = testThroughputTCP(256 * 1024)));
                    Thread.sleep(500);
                    System.out.println("Client: TCP Throughput (1M bytes): " + (throughputTCP[4][i] = testThroughputTCP(1024 * 1024)));
                    Thread.sleep(500);
                }

                // Save results to file and compute averages
                output.write("TCP Throughput\n");
                double[] avgThroughputTCP = new double[5];
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < TRIALS; j++) {
                        output.write(throughputTCP[i][j] + "\n");
                        avgThroughputTCP[i] += throughputTCP[i][j];
                    }
                    avgThroughputTCP[i] /= TRIALS;
                    output.write("\n");
                }

                // Print averages
                System.out.println("Client: Average TCP Throughput (1K bytes): " + avgThroughputTCP[0]);
                System.out.println("Client: Average TCP Throughput (16K bytes): " + avgThroughputTCP[1]);
                System.out.println("Client: Average TCP Throughput (64K bytes): " + avgThroughputTCP[2]);
                System.out.println("Client: Average TCP Throughput (256K bytes): " + avgThroughputTCP[3]);
                System.out.println("Client: Average TCP Throughput (1M bytes): " + avgThroughputTCP[4]);

            } else if (experiment == INTERACTION_TCP) {
                // Do TCP interaction experiment
                for (int i = 0; i < TRIALS; i++) {
                    System.out.println("\nClient: TCP Interaction (1024 x 1024 bytes): " + (interactionTCP[0][i] = testInteractionTCP(1024, 1024)));
                    Thread.sleep(1000);
                    System.out.println("Client: TCP Interaction (2048 x 512 bytes): " + (interactionTCP[1][i] = testInteractionTCP(2048, 512)));
                    Thread.sleep(1000);
                    System.out.println("Client: TCP Interaction (4096 x 256 bytes): " + (interactionTCP[2][i] = testInteractionTCP(4096, 256)));
                    Thread.sleep(1000);
                }

                // Save results to file and compute averages
                output.write("TCP Interaction\n");
                double[] avgInteractionTCP = new double[3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < TRIALS; j++) {
                        output.write(interactionTCP[i][j] + "\n");
                        avgInteractionTCP[i] += interactionTCP[i][j];
                    }
                    avgInteractionTCP[i] /= TRIALS;
                    output.write("\n");
                }

                // Print averages
                System.out.println("Client: Average TCP Interaction (1024 x 1024 bytes): " + avgInteractionTCP[0]);
                System.out.println("Client: Average TCP Interaction (2048 x 512 bytes): " + avgInteractionTCP[1]);
                System.out.println("Client: Average TCP Interaction (4096 x 256 bytes): " + avgInteractionTCP[2]);

            } else if (experiment == INTERACTION_UDP) {
                // Do UDP interaction experiment
                for (int i = 0; i < TRIALS; i++) {
                    System.out.println("\nClient: UDP Interaction (1024 x 1024 bytes): " + (interactionUDP[0][i] = testInteractionUDP(1024, 1024)));
                    Thread.sleep(1000);
                    System.out.println("Client: UDP Interaction (2048 x 512 bytes): " + (interactionUDP[1][i] = testInteractionUDP(2048, 512)));
                    Thread.sleep(1000);
                    System.out.println("Client: UDP Interaction (4096 x 256 bytes): " + (interactionUDP[2][i] = testInteractionUDP(4096, 256)));
                    Thread.sleep(1000);
                }

                // Save results to file and compute averages
                output.write("UDP Interaction\n");
                double[] avgInteractionUDP = new double[3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < TRIALS; j++) {
                        output.write(interactionUDP[i][j] + "\n");
                        avgInteractionUDP[i] += interactionUDP[i][j];
                    }
                    avgInteractionUDP[i] /= TRIALS;
                    output.write("\n");
                }

                // Print averages
                System.out.println("Client: Average UDP Interaction (1024 x 1024 bytes): " + avgInteractionUDP[0]);
                System.out.println("Client: Average UDP Interaction (2048 x 512 bytes): " + avgInteractionUDP[1]);
                System.out.println("Client: Average UDP Interaction (4096 x 256 bytes): " + avgInteractionUDP[2]);
            }

            output.close();
            System.out.println("\nClient: DONE!");

        } catch (IOException ex) {
            System.out.println("Client: Error: IO!");
            ex.printStackTrace();
            System.exit(1);
        } catch (InterruptedException ex) {
            System.out.println("Client: Error: Interrupted while sleeping!");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public long testLatencyTCP(int size) {
        long latency = 0;

        try {
            // Open a connection with the server
            socketTCP = new Socket(host, PORT_TCP);
            DataOutputStream out = new DataOutputStream(socketTCP.getOutputStream());
            DataInputStream in = new DataInputStream(socketTCP.getInputStream());

            // Generate a message
            String input = "Testing TCP latency................";
            byte[] msg = input.getBytes(StandardCharsets.UTF_8);
            byte[] xor = xorEncrypt(msg, "CSCFourFourFive".getBytes(StandardCharsets.UTF_8), size);

            // Start the timer
            long startTime = System.nanoTime();

            // Send the message
            out.write(xor);
            out.flush();

            // Wait for a response
            msg = new byte[size];
            int bytes = in.read(msg, 0, size);

            // Measure the latency
            latency = System.nanoTime() - startTime;

            System.out.println("Client: Received " + bytes + " bytes!");

            // Close the connection
            out.close();
            in.close();
            socketTCP.close();

        } catch (UnknownHostException ex) {
            System.out.println("Client: Error: Tried to connect to unknown host!");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Client: Error: IO!");
            ex.printStackTrace();
            System.exit(1);
        }

        return latency;
    }

    public long testLatencyUDP(int size) {
        long latency = 0;

        try {
            // Open a connection with the server
            socketUDP = new DatagramSocket(PORT_UDP);

            // Generate a message
            String input = "Testing UDP latency................";
            byte[] msg = input.getBytes(StandardCharsets.UTF_8);
            byte[] xor = xorEncrypt(msg, "CSCFourFourFive".getBytes(StandardCharsets.UTF_8), size);
            DatagramPacket packet = new DatagramPacket(xor, size, InetAddress.getByName(host), PORT_UDP);

            // Start the timer
            long startTime = System.nanoTime();

            // Send the message
            sendWithTimeout(packet, 2000, 1);

            // Wait for a response
            msg = new byte[size];
            packet = new DatagramPacket(msg, size);
            receiveWithTimeout(packet, 2000, 1);

            // Measure the latency
            latency = System.nanoTime() - startTime;

            System.out.println("Client: Received " + packet.getLength() + " bytes!");

            // Close the connection
            socketUDP.close();

        } catch (UnknownHostException ex) {
            System.out.println("Client: Error: Tried to connect to unknown host!");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Client: Error: IO!");
            ex.printStackTrace();
            System.exit(1);
        }

        return latency;
    }

    public int testThroughputTCP(int size) {
        int throughput = 0;

        try {
            // Open a connection with the server
            socketTCP = new Socket(host, PORT_TCP);
            DataOutputStream out = new DataOutputStream(socketTCP.getOutputStream());
            DataInputStream in = new DataInputStream(socketTCP.getInputStream());

            // Generate a message
            String input = "Testing TCP throughput................";
            byte[] msg = input.getBytes(StandardCharsets.UTF_8);
            byte[] xor = xorEncrypt(msg, "CSCFourFourFive".getBytes(StandardCharsets.UTF_8), size);

            // Start the timer
            long startTime = System.nanoTime();

            // Repeatedly send the message until time runs out
            while ((System.nanoTime() - startTime) < 1000000000) {

                // Send message
                out.write(xor);
                out.flush();

                // Wait for a response
                msg = new byte[size];
                in.readFully(msg, 0, size);

                throughput++;
            }

            // Send a "CLOSE" message
            msg[0] = 'C';
            msg[1] = 'L';
            msg[2] = 'O';
            msg[3] = 'S';
            msg[4] = 'E';
            out.write(msg);
            out.flush();

            // Close the connection
            out.close();
            in.close();
            socketTCP.close();

        } catch (UnknownHostException ex) {
            System.out.println("Client: Error: Tried to connect to unknown host!");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Client: Error: IO!");
            ex.printStackTrace();
            System.exit(1);
        }

        return throughput;
    }

    public long testInteractionTCP(int messages, int size) {
        long latency = 0;

        try {
            // Open a connection with the server
            socketTCP = new Socket(host, PORT_TCP);
            DataOutputStream out = new DataOutputStream(socketTCP.getOutputStream());
            DataInputStream in = new DataInputStream(socketTCP.getInputStream());

            // Generate a message
            String input = "Testing TCP interaction................";
            byte[] msg = input.getBytes(StandardCharsets.UTF_8);
            byte[] xor = xorEncrypt(msg, "CSCFourFourFive".getBytes(StandardCharsets.UTF_8), size);

            // Start the timer
            long startTime = System.nanoTime();

            for (int i = 0; i < messages; i++) {
                // Send message
                out.write(xor);
                out.flush();

                // Wait for response
                msg = new byte[8];
                in.read(msg, 0, 8);
            }

            // Measure the latency
            latency = System.nanoTime() - startTime;

            // Close the connection
            out.close();
            in.close();
            socketTCP.close();

        } catch (UnknownHostException ex) {
            System.out.println("Client: Error: Tried to connect to unknown host!");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Client: Error: IO!");
            ex.printStackTrace();
            System.exit(1);
        }

        return latency;
    }

    public long testInteractionUDP(int messages, int size) {
        long latency = 0;

        try {
            // Open a connection with the server
            socketUDP = new DatagramSocket(PORT_UDP);

            // Generate a message
            String input = "Testing UDP throughput................";
            byte[] msg = input.getBytes(StandardCharsets.UTF_8);
            byte[] xor = xorEncrypt(msg, "CSCFourFourFive".getBytes(StandardCharsets.UTF_8), size);

            // Start the timer
            long startTime = System.nanoTime();

            // Send messages
            for (int i = 0; i < messages; i++) {
                DatagramPacket packet = new DatagramPacket(xor, size, InetAddress.getByName(host), PORT_UDP);
                sendWithTimeout(packet, 2000, 1);

                // Wait for a response
                msg = new byte[8];
                packet = new DatagramPacket(msg, 8);
                receiveWithTimeout(packet, 2000, 1);
            }

            // Measure the latency
            latency = System.nanoTime() - startTime;

            // Close the connection
            socketUDP.close();

        } catch (UnknownHostException ex) {
            System.out.println("Client: Error: Tried to connect to unknown host!");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Client: Error: IO!");
            ex.printStackTrace();
            System.exit(1);
        }

        return latency;
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

    public byte[] xorEncrypt(byte[] message, byte[] key, int size) {
        byte[] result = new byte[size];

        for(int i=0; i<size; i++)
            if (i < message.length)
                result[i] = (byte) (message[i] ^ key[i % key.length]);
            else
                result[i] = ' ';

        return result;
    }

    public static void main(String[] args) throws Exception {
        new TesterClient();
    }
}
