import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

/**
 * Assignment: CSC375 Assignment 1
 * Author: Vitaliy Shydlonok
 * Date: 10/2/2019
 */

public class FactoryOptimizer {

    private OptimizerUI gui;

    public static final int FLOOR_WIDTH = 20; // The width of each factory floor
    public static final int FLOOR_HEIGHT = 15; // The height of each factory floor
    public static final int NUM_SPOTS = FLOOR_WIDTH*FLOOR_HEIGHT; // The number of spots each factory floor contains
    public static final int NUM_STATIONS = 256; // The ideal number of units that aren't empty on each factory floor
    public static final int NUM_FLAVORS = 4; // The number of flavors of units
    public static final int NUM_MIN_STATIONS = 8; // The minumum number of each flavor that should be on a floor
    public static final int NUM_THREADS = 128; // The number of threads to run concurrently

    private static final int[][] affinity = {{1, 2, 3, 3}, {2, 0, 1, 3}, {3, 1, 1, 2}, {3, 3, 2, 0}}; // Affinity function between units

    private ExecutorService executor;
    private Exchanger exchanger;

    public FactoryOptimizer() {
        // Set up the UI before doing anything
        try {
            FactoryOptimizer optimizer = this;
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    gui = new OptimizerUI(optimizer);
                }
            });
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Starts a number of OptimizerThread threads
     */
    public synchronized void startThreads() {
        executor = Executors.newFixedThreadPool(NUM_THREADS);
        exchanger = new Exchanger();

        // Start a number of optimization threads
        for(int i=0; i<NUM_THREADS; i++) {
            OptimizerThread thread = new OptimizerThread(i, exchanger, gui);
            executor.submit(thread);
        }
    }

    /**
     * Shuts down the executor as fast as possible
     */
    public synchronized void resetThreads() {
        if (isRunning())
            executor.shutdownNow();
    }

    /**
     * Returns whether the concurrent optimization is running
     * @return true if the executor exists and hasn't been shut down, false otherwise
     */
    public boolean isRunning() {
        return (executor != null && !executor.isShutdown());
    }

    /**
     * Returns the affinity of one factory unit in relation to another
     * @param first first factory unit
     * @param second second factory unit
     * @return affinity between the two units
     */
    public static int getAffinity(int first, int second) {
        return affinity[first][second];
    }

    /**
     * Program entry
     * @param args
     */
    public static void main(String[] args) {
        new FactoryOptimizer();
    }
}
