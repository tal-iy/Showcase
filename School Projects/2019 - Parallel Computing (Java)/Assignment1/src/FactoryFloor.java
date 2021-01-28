import java.util.concurrent.ThreadLocalRandom;

public class FactoryFloor implements Comparable<FactoryFloor>{

    private int[][] floorUnits; // Grid representing the factory floor units

    public FactoryFloor() {
        // Create a grid of random units
        floorUnits = new int[FactoryOptimizer.FLOOR_WIDTH][FactoryOptimizer.FLOOR_HEIGHT];
        for(int x=0; x<FactoryOptimizer.FLOOR_WIDTH; x++) {
            for(int y=0; y<FactoryOptimizer.FLOOR_HEIGHT; y++) {
                floorUnits[x][y] = ThreadLocalRandom.current().nextInt(1, FactoryOptimizer.NUM_FLAVORS);
            }
        }

        // Make sure there is a minimum number of empty spaces on the floor
        for(int i=0; i<FactoryOptimizer.NUM_SPOTS-FactoryOptimizer.NUM_STATIONS; i++) {
            // Choose a random spot on the floor
            int x = ThreadLocalRandom.current().nextInt(0, FactoryOptimizer.FLOOR_WIDTH);
            int y = ThreadLocalRandom.current().nextInt(0, FactoryOptimizer.FLOOR_HEIGHT);

            // Make sure that the chosen spot doesn't have an empty space already
            while(floorUnits[x][y] == 0) {
                x = ThreadLocalRandom.current().nextInt(0, FactoryOptimizer.FLOOR_WIDTH);
                y = ThreadLocalRandom.current().nextInt(0, FactoryOptimizer.FLOOR_HEIGHT);
            }
            floorUnits[x][y] = 0;
        }
    }

    /**
     * Returns the factory floor unit at a location on the floor
     * @param x x-position of the unit to retrieve
     * @param y y-position of the unit to retrieve
     * @return the factory unit at position (x, y)
     */
    public int getAt(int x, int y) {
        return floorUnits[x][y];
    }

    /**
     * Replaces a unit on the factory floor with a new unit
     * @param x x-position of the unit to replace
     * @param y y-position of the unit to replace
     * @param value the new unit
     */
    public void setAt(int x, int y, int value) {
        floorUnits[x][y] = value;
    }

    /**
     * Copies the current factory floor onto a new factory floor
     * @return a copy of the current factory floor
     */
    public FactoryFloor copy() {
        FactoryFloor floor = new FactoryFloor();
        for(int x=0; x<FactoryOptimizer.FLOOR_WIDTH; x++) {
            for(int y=0; y<FactoryOptimizer.FLOOR_HEIGHT; y++) {
                floor.setAt(x, y, floorUnits[x][y]);
            }
        }
        return floor;
    }

    /**
     * Swaps two random locations on the factory floor
     */
    public void swapRandom() {
        // Pick two random locations on the floor
        int x1 = ThreadLocalRandom.current().nextInt(0, FactoryOptimizer.FLOOR_WIDTH);
        int y1 = ThreadLocalRandom.current().nextInt(0, FactoryOptimizer.FLOOR_HEIGHT);
        int x2 = ThreadLocalRandom.current().nextInt(0, FactoryOptimizer.FLOOR_WIDTH);
        int y2 = ThreadLocalRandom.current().nextInt(0, FactoryOptimizer.FLOOR_HEIGHT);

        // Swap the units at the chosen locations
        int tempUnit = floorUnits[x1][y1];
        floorUnits[x1][y1] = floorUnits[x2][y2];
        floorUnits[x2][y2] = tempUnit;
    }

    /**
     * Calculates the total fitness of the factory floor
     * @return the total fitness score
     */
    public int getTotalFitness() {
        int fitness = 0;
        int numEmpty = 0;
        int[] numStations = new int[FactoryOptimizer.NUM_FLAVORS-1];

        // Loop through every spot on the floor and add each units fitness to the total
        for(int y=0; y<FactoryOptimizer.FLOOR_HEIGHT; y++) {
            for(int x=0; x<FactoryOptimizer.FLOOR_WIDTH; x++) {
                fitness += getUnitFitness(x, y);
                if (floorUnits[x][y] == 0)
                    numEmpty++;
                else
                    numStations[floorUnits[x][y]-1]++;
            }
        }

        // Subtract from the total if there are too many or not enough empty spaces
        fitness -= Math.abs(numEmpty - (FactoryOptimizer.NUM_SPOTS-FactoryOptimizer.NUM_STATIONS))*20;

        // Subtract from the total if there are too few units of each type
        for(int i=0; i<numStations.length; i++)
            fitness -= (numStations[i] < FactoryOptimizer.NUM_MIN_STATIONS) ? 100 : 0;


        return fitness;
    }

    /**
     * Calculates the fitness of an individual unit on the factory floor
     * @param x x-position of the unit
     * @param y y-position of the unit
     * @return the fitness of the unit
     */
    private int getUnitFitness(int x, int y) {
        int fitness = 0;

        // Calculate the top row
        if (x > 0 && y > 0)
            fitness += FactoryOptimizer.getAffinity(floorUnits[x][y], floorUnits[x-1][y-1]);
        if (y > 0)
            fitness += FactoryOptimizer.getAffinity(floorUnits[x][y], floorUnits[x][y-1]);
        if (x < FactoryOptimizer.FLOOR_WIDTH-1 && y > 0)
            fitness += FactoryOptimizer.getAffinity(floorUnits[x][y], floorUnits[x+1][y-1]);

        // Calculate the middle row
        if (x > 0)
            fitness += FactoryOptimizer.getAffinity(floorUnits[x][y], floorUnits[x-1][y]);
        if (x < FactoryOptimizer.FLOOR_WIDTH-1)
            fitness += FactoryOptimizer.getAffinity(floorUnits[x][y], floorUnits[x+1][y]);

        // Calculate the bottom row
        if (x > 0 && y < FactoryOptimizer.FLOOR_HEIGHT-1)
            fitness += FactoryOptimizer.getAffinity(floorUnits[x][y], floorUnits[x-1][y+1]);
        if (y < FactoryOptimizer.FLOOR_HEIGHT-1)
            fitness += FactoryOptimizer.getAffinity(floorUnits[x][y], floorUnits[x][y+1]);
        if (x < FactoryOptimizer.FLOOR_WIDTH-1 && y < FactoryOptimizer.FLOOR_HEIGHT-1)
            fitness += FactoryOptimizer.getAffinity(floorUnits[x][y], floorUnits[x+1][y+1]);

        return fitness;
    }

    /**
     * Compares two FactoryFloor objects based on their fitness values
     * @param o the other FactoryFloor to compare to
     * @return the difference between the two fitness values
     */
    @Override
    public int compareTo(FactoryFloor o) {
        return o.getTotalFitness()-getTotalFitness();
    }
}
