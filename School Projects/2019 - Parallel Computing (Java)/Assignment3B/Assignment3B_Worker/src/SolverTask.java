import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicLong;

public class SolverTask extends CountedCompleter<Long> {

    private static int MIN_RANGE = 100;
    private AtomicLong result;
    private long lo;
    private long hi;
    private final long state;
    private final Flip flip;

    public SolverTask(CountedCompleter<Long> parent, Flip flip, AtomicLong result, long lo, long hi, long state) {
        super(parent);
        this.result = result;
        this.lo = lo;
        this.hi = hi;
        this.state = state;
        this.flip = flip;
    }

    @Override
    public Long getRawResult () {
        return result.get();
    }

    @Override
    public void compute() {
        if (hi-lo < MIN_RANGE) {
            findBestMoves();
            propagateCompletion();
        } else {
            // Find the new middle
            long middle = lo+((hi-lo) >> 1);

            // Fork the right half
            addToPendingCount(1);
            new SolverTask(this, flip, result, middle, hi, state).fork();

            // Solve the left half
            hi = middle;
            this.compute();
        }
    }

    public void findBestMoves() {
        // Loop through the state space between lo and hi
        for(long i=lo;i<hi;i++) {
            // Search for the best possible move set
            if (Flip.checkMoves(state, i)) {
                System.out.println("Worker: Correct I - "+Long.toBinaryString(i));

                // Calculate the number of moves the new move set has
                int numMovesNew = Flip.countMoves(i);

                // Keep trying to change the result
                boolean replaced = false;
                boolean replacing = true;
                long res;
                int numMovesOld;
                do {
                    // Get the current result
                    res = result.get();

                    // Calculate the number of moves the new move set has
                    numMovesOld = Flip.countMoves(res);

                    if (numMovesNew < numMovesOld || res == 0)
                        replaced = result.compareAndSet(res, i);
                    else
                        replacing = false;

                    if (!replaced && replacing) {
                        System.out.print("Worker: Failed to replace: "+Long.toBinaryString(res)+"-"+numMovesOld+" with: "+Long.toBinaryString(i)+"-"+numMovesNew);
                        res = result.get();
                        System.out.println(", it was: " + Long.toBinaryString(res));
                    } else if (replacing){
                        System.out.println("Worker: Successfully replaced: "+Long.toBinaryString(res)+"-"+numMovesOld+" with: "+Long.toBinaryString(i)+"-"+numMovesNew);
                    } else {
                        System.out.println("Worker: Successfully kept: "+Long.toBinaryString(res)+"-"+numMovesOld+", rejecting: "+Long.toBinaryString(i)+"-"+numMovesNew);
                    }

                } while (!replaced && replacing);
            }
        }
    }
}
