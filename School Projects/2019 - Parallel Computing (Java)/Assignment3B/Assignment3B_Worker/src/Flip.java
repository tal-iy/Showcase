import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;

public class Flip {

    public static final int GAME_WIDTH = 5;
    public static final int GAME_HEIGHT = 5;

    public static int port;

    private boolean shuttingDown = false;

    public static void main(String[] args) {

        // Make sure that a port was given
        if (args.length < 1) {
            System.out.println("Worker Error: Undefined worker port");
            System.exit(1);
        }

        port = Integer.parseInt(args[0]);
        if (port < 0 || port > 65535) throw new NumberFormatException();

        new Flip();
    }

    public Flip() {
        String ip = "";
        try { ip = (new BufferedReader(new InputStreamReader(new URL("http://bot.whatismyipaddress.com").openStream()))).readLine().trim(); }
        catch (Exception e)
        {
            System.out.println("Worker Error: Cannot find public IP address!");
            System.exit(1);
        }

        System.out.println("Worker: Starting at ip "+ip+", listening on port number " + port);

        while (!shuttingDown) {

            // Create a socket that listens for connections
            ServerSocket listener = null;
            try { listener = new ServerSocket(port); }
            catch (Exception e) {
                System.out.println("Worker Error: Could not open a listening socket on port " + port);
                System.exit(1);
            }

            // Wait for a connection and close the listening socket
            try {
                Socket connection = listener.accept();
                listener.close();
                System.out.println("Worker: Accepted connection from " + connection.getInetAddress());
                handleConnection(connection);
            }
            catch (Exception e) {
                System.out.println("Worker Error: Server shut down!");
                System.exit(1);
        }
        }

        System.out.println("Worker: Shutting down...");
    }

    private void handleConnection(Socket connection) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintWriter out = new PrintWriter(connection.getOutputStream());

            while (true) {
                // Wait for a message
                String message = in.readLine();

                if (message == null) { throw new IOException("Worker Error: Disconnected from server!"); }

                if (message.startsWith("close")) {
                    System.out.println("Worker: Closing connection...");
                    break;
                }
                else if (message.startsWith("shutdown")) {
                    shuttingDown = true;
                    break;
                }
                else if (message.startsWith("work")) {

                    // Parse the message
                    Scanner scanner = new Scanner(message);
                    scanner.next(); // Skip "work"
                    long data = Long.parseLong(scanner.next(), 16);
                    long lo = Long.parseLong(scanner.next(), 16);
                    long hi = Long.parseLong(scanner.next(), 16);

                    // Compute
                    long result = solve(data, lo, hi);

                    // Send back the result
                    out.println("result "+Long.toHexString(result));
                    out.flush();
                }
                else throw new Exception("Worker Error: Server sent illegal message: "+message);
            }
        }
        catch (Exception e) {
            System.out.println("Worker Error: Connection closed with error " + e);
        }
        finally { try { connection.close(); } catch (Exception e) { } }
    }

    public static boolean checkMoves(long state, long moves) {

        boolean[][] stateGrid = convertIntToGrid(state);
        boolean[][] movesGrid = convertIntToGrid(moves);

        // Simulate the grid of moves
        for(int y=0; y<Flip.GAME_HEIGHT; y++) {
            for (int x = 0; x < Flip.GAME_WIDTH; x++) {
                if (movesGrid[x][y]) {
                    if (x > 0) stateGrid[x-1][y] = !stateGrid[x-1][y];
                    if (y > 0) stateGrid[x][y-1] = !stateGrid[x][y-1];
                    if (x < Flip.GAME_WIDTH-1) stateGrid[x+1][y] = !stateGrid[x+1][y];
                    if (y < Flip.GAME_HEIGHT-1) stateGrid[x][y+1] = !stateGrid[x][y+1];
                    stateGrid[x][y] = !stateGrid[x][y];
                }
            }
        }

        // Check if the game is solved
        boolean solved = true;
        for(int y=0; y<Flip.GAME_HEIGHT; y++) {
            for (int x = 0; x < Flip.GAME_WIDTH; x++) {
                if (stateGrid[x][y]) {
                    solved = false;
                    break;
                }
            }
        }

        return solved;
    }

    private static boolean[][] convertIntToGrid(long data) {
        String dataString = Long.toBinaryString(data);
        boolean[][] grid = new boolean[GAME_WIDTH][GAME_HEIGHT];

        for(int y=0; y<Flip.GAME_HEIGHT; y++) {
            for (int x = 0; x < Flip.GAME_WIDTH; x++) {
                // Convert coordinates to an index in the binary string
                int gridIndex = dataString.length()-1-(x+(y*GAME_WIDTH));
                if (gridIndex < 0) grid[x][y] = false;
                else grid[x][y] = (dataString.charAt(gridIndex) == '1');
            }
        }

        return grid;
    }

    public static int countMoves(long moves) {
        int count = 0;
        while (moves > 0) {
            count += moves & 1;
            moves >>= 1;
        }
        return count;
    }

    public long solve(long data, long lo, long hi) {

        // Split work using a counted completer
        long result = ForkJoinPool.commonPool().invoke(new SolverTask(null, this, new AtomicLong(0), lo, hi, data));

        // Make sure a solution was found
        if (result == 0) System.out.println("Worker: No Solution!");
        else System.out.println("Worker: Final Solution - " + Long.toBinaryString(result));

        return result;
    }
}
