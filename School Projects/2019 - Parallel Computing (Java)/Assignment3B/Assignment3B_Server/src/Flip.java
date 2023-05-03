import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Flip {

    public static final int GAME_WIDTH = 5;
    public static final int GAME_HEIGHT = 5;

    public static int workerNum = 2;
    public static String[] workerIps;
    public static int[] workerPorts;

    private GameWindow window;

    private volatile boolean computing = false;
    private volatile AtomicLong result = new AtomicLong(0L);
    private volatile AtomicInteger resultNum = new AtomicInteger(0);

    public static void main(String[] args) {

        // Make sure at least one worker ip:port was given
        if (args.length < 1) {
            System.out.println("Server Error: Undefined worker ip and port");
            System.exit(1);
        }

        workerNum = args.length;
        workerIps = new String[workerNum];
        workerPorts = new int[workerNum];

        // Get worker ips and ports from the program arguments
        for (int i = 0; i < workerNum; i++) {
            System.out.println(args[i]);
            int pos = args[i].indexOf(':');
            if (pos >= 0) {
                workerIps[i] = args[i].substring(0,pos);
                workerPorts[i] = Integer.parseInt(args[i].substring(pos+1));
            }
        }

        new Flip();
    }

    public Flip() {
        window = new GameWindow(this);
        window.randomizeGrid();
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

    public void solve(long data) {

        // Reset the atomic result
        result.set(0L);
        resultNum.set(0);
        computing = true;

        // Create a worker thread for every available worker
        for(int i = 0; i< workerNum; i++) {

            // Split the work evenly between workers
            long chunkSize = (long) (Math.pow(2, GAME_WIDTH * GAME_HEIGHT)/ workerNum);
            long lo = i * chunkSize;
            long hi = (i+1) * chunkSize;

            new SolverThread(this, i, workerIps[i], workerPorts[i], data, lo, hi);
        }
    }

    public void updateSolution(long moves) {

        // Calculate the number of moves the new move set has
        int numMovesNew = Flip.countMoves(moves);

        // Keep trying to change the result
        boolean replaced = false;
        boolean replacing = true;
        long res;
        int numMovesOld;
        do {
            // Get the current result
            res = result.get();

            // Calculate the number of moves the old move set has
            numMovesOld = Flip.countMoves(res);

            if (numMovesNew < numMovesOld || res == 0)
                replaced = result.compareAndSet(res, moves);
            else
                replacing = false;

            if (!replaced && replacing) {
                System.out.print("Server: Failed to replace: "+Long.toBinaryString(res)+"-"+numMovesOld+" with: "+Long.toBinaryString(moves)+"-"+numMovesNew);
                res = result.get();
                System.out.println(", it was: " + Long.toBinaryString(res));
            } else if (replacing){
                System.out.println("Server: Successfully replaced: "+Long.toBinaryString(res)+"-"+numMovesOld+" with: "+Long.toBinaryString(moves)+"-"+numMovesNew);
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            window.setSolution(result.get());
                        }
                    });
                } catch (InterruptedException | InvocationTargetException e) { e.printStackTrace(); }
            } else {
                System.out.println("Server: Successfully kept: "+Long.toBinaryString(res)+"-"+numMovesOld+", rejecting: "+Long.toBinaryString(moves)+"-"+numMovesNew);
            }

        } while (!replaced && replacing);

        if (resultNum.incrementAndGet() >= workerNum) {

            // Make sure a solution was found
            final long lastRes = result.get();
            if (lastRes == 0) {

                System.out.println("Server: No Solution!");
                Object[] options = {"Randomize", "Cancel"};
                int n = JOptionPane.showOptionDialog(window, "No solution found!", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == 0) window.randomizeGrid();

            } else {

                // Update the game window with the final solution
                System.out.println("Server: Final Solution: " + Long.toBinaryString(lastRes));
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        computing = false;
                        window.setSolution(lastRes);
                    }
                });
            }
        }
    }

    public boolean isComputing() {
        return computing;
    }
}
