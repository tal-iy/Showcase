import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;

public class OptimizerThread implements Runnable {

    public static final int NUM_FLOORS = 34; // The number of factory floors in the population
    public static final int NUM_PARENTS = 6; // The number of parents to use every generation
    public static final int NUM_SWAPS = 2; // The number of times to mutate every generation
    public static final int CHANCE_PARENT = 80; // The % chance of the best floors becoming a parent

    public static final int GEN_INTERVAL = 20; // The number of generations between exchanges with other threads
    public static final int GEN_MAX = 800; // The maximum number of generations to calculate

    private int generation = 1;

    private ArrayList<FactoryFloor> floors;
    private ArrayList<FactoryFloor> parents;
    private FactoryFloor best;

    private Exchanger exchanger;
    private int id;
    private OptimizerUI gui;

    public OptimizerThread(int id, Exchanger exchanger, OptimizerUI gui) {
        this.exchanger = exchanger;
        this.id = id;
        this.gui = gui;

        // Create a number of random factory floors to start with
        floors = new ArrayList<>();
        for(int floor=0; floor<NUM_FLOORS; floor++)
            floors.add(new FactoryFloor());

        parents = new ArrayList<>();
    }

    /**
     * Simulates the genetic algorithm:
     *  1. Sort the floors by fitness
     *  2. Exchange the best floor periodically
     *  3. Sort again by fitness
     *  4. Mate the best floors with probability
     *  5. Mutate all floors
     */
    @Override
    public void run() {
        try {
            while(generation <= GEN_MAX) {

                // Update the thread status of the GUI every generation
                int gen = generation;
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        gui.updateThread(id, gen);
                    }
                });

                // Exchange the best floor periodically
                if (generation % GEN_INTERVAL == 0) {

                    Collections.sort(floors);

                    // Update the overall best floor that the GUI has periodically
                    if (generation % (GEN_INTERVAL*5) == 0 && gui.bestScore < floors.get(0).getTotalFitness()) {
                        FactoryFloor best = floors.get(0).copy();
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                gui.updateBest(best);
                            }
                        });
                    }

                    // Only try to trade for one second, ignore failures
                    try {
                        floors.set(0, (FactoryFloor) exchanger.exchange(floors.get(0), 1, TimeUnit.SECONDS));
                    } catch (TimeoutException ex) { }

                } else {
                    // Genetic Algorithm
                    Collections.sort(floors);
                    mateAllFloors();
                    mutateFloors();
                }
                generation++;
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {

            // Tell the GUI that the thread finished
            int gen = generation;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    gui.updateFinished(id, gen);
                }
            });
        }
    }

    /**
     * Mutates every floor a number of times
     */
    private void mutateFloors() {
        // Go through every floor
        for (int floor=0; floor<NUM_FLOORS; floor++) {
            // Do a swap multiple times
            for(int swap=0; swap<NUM_SWAPS; swap++) {
                floors.get(floor).swapRandom();
            }
        }
    }

    /**
     * Chooses a number of the best floors with probability and mates them,
     * then uses the parents and children for the next generation
     */
    private void mateAllFloors() {
        // Empty the list of parents
        parents.clear();

        // Take the best ever
        if (best == null || floors.get(0).getTotalFitness() > best.getTotalFitness()) {
            best = floors.get(0);
        }

        // Take the best parents with a probability, making sure that we get enough parents
        for(int floor=0; floor<NUM_FLOORS && parents.size()<NUM_PARENTS; floor++) {
            if (ThreadLocalRandom.current().nextInt(0, 100) < CHANCE_PARENT || NUM_PARENTS-parents.size() >= NUM_FLOORS-floor) {
                parents.add(floors.get(floor));
            }
        }

        // Clear the pool of factory floors
        floors.clear();
        floors.add(best);
        floors.addAll(parents);

        // Mate until there are enough children
        while(floors.size() < NUM_FLOORS) {
            // Pick two unique parents at random
            int choice1 = ThreadLocalRandom.current().nextInt(0, parents.size());
            int choice2 = ThreadLocalRandom.current().nextInt(0, parents.size());
            while(choice2 == choice1)
                choice2 = ThreadLocalRandom.current().nextInt(0, parents.size());

            // Mate the two chosen
            mateTwoFloors(parents.get(choice1), parents.get(choice2));
        }
    }

    /**
     * Mates two FactoryFloor objects by randomly swapping units at each position
     * @param floor1 first floor to mate
     * @param floor2 second floor to mate
     */
    private void mateTwoFloors(FactoryFloor floor1, FactoryFloor floor2) {
        FactoryFloor child = new FactoryFloor();
        for(int x=0; x<FactoryOptimizer.FLOOR_WIDTH; x++) {
            for(int y=0; y<FactoryOptimizer.FLOOR_HEIGHT; y++) {
                if (ThreadLocalRandom.current().nextInt(0, 2) != 0) {
                    child.setAt(x, y, floor1.getAt(x, y));
                } else {
                    child.setAt(x, y, floor2.getAt(x, y));
                }
            }
        }
        floors.add(child);
    }
}
