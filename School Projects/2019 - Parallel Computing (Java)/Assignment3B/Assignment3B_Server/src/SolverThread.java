import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SolverThread extends Thread {
    private final Flip flip;

    private final int id;
    private final String ip;
    private final int port;

    private final long data;
    private final long lo;
    private final long hi;

    SolverThread(Flip flip, int id, String ip, int port, long data, long lo, long hi) {
        this.flip = flip;

        this.id = id;
        this.ip = ip;
        this.port = port;

        this.data = data;
        this.lo = lo;
        this.hi = hi;

        start();
    }

    public static void shutdown(String ip, int port) {

        // Connect to the worker
        Socket socket;
        try { socket = new Socket(ip, port); } catch (Exception e) {
            System.out.println("Server Error: Could not shut down " + ip + ":" + port);
            e.printStackTrace();
            return;
        }

        try {

            // Send shutdown command to the worker
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("shutdown");
            out.flush();

        } catch (Exception e) { e.printStackTrace(); } finally {
            try { socket.close(); } catch (Exception e) { }
        }
    }

    public void run() {

        long result = 0L;

        // Connect to the worker
        Socket socket;
        try { socket = new Socket(ip, port); } catch (Exception e) {
            System.out.println("Server Error: Thread " + id + " could not open connection to " + ip + ":" + port);
            e.printStackTrace();
            return;
        }

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send work to the worker
            out.println("work "+Long.toHexString(data)+" "+Long.toHexString(lo)+" "+Long.toHexString(hi));
            out.flush();

            // Wait for a result
            String message = in.readLine();

            // Validate the result
            if (message == null) throw new IOException("Server Error: Disconnected from worker!");
            if (!message.startsWith("result")) throw new IOException("Server Error: Worker sent illegal message: "+message);

            // Parse the result
            Scanner scanner = new Scanner(message);
            scanner.next();
            String resultString = scanner.next();
            result = Long.parseLong(resultString, 16);

            // Tell the worker to close
            out.println("close");
            out.flush();
        } catch (Exception e) { e.printStackTrace(); } finally {

            System.out.println("Server: Thread " + id + " finished with result: ");

            // Update the game with the new result
            flip.updateSolution(result);

            try { socket.close(); } catch (Exception e) { }
        }
    }
}
