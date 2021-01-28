import javax.swing.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;

public class Flip {

    public static final int GAME_WIDTH = 5;
    public static final int GAME_HEIGHT = 5;

    private GameWindow window;
    private volatile boolean computing = false;

    public static void main(String[] args) {
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

        // Make a new thread so that the fork-join tasks can invokeAndWait on the GUI
        computing = true;
        final Flip flip = this;
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Split work using a counted completer
                long result = ForkJoinPool.commonPool().invoke(new SolverTask(null, flip, new AtomicLong(0), 0, (long) Math.pow(2, GAME_WIDTH * GAME_HEIGHT), data));

                // Make sure a solution was found
                if (result == 0) {
                    System.out.println("No Solution!");
                    Object[] options = {"Randomize", "Cancel"};
                    int n = JOptionPane.showOptionDialog(window, "No solution found!", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (n == 0) window.randomizeGrid();
                } else {

                    // Update the game window with the final solution
                    System.out.println("Final Solution: " + Long.toBinaryString(result));
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            computing = false;
                            window.setSolution(result);
                        }
                    });
                }

            }
        }).start();
    }

    public void updateSolution(long moves) {
        window.setSolution(moves);
    }

    public boolean isComputing() {
        return computing;
    }
}
