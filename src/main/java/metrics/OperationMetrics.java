package metrics;

public class OperationMetrics {

    private long steps;
    private long moves;
    private long comparisons;

    public void incrementSteps() {
        steps++;
    }

    public void incrementMoves() {
        moves++;
    }

    public void incrementComparisons() {
        comparisons++;
    }

    public long getSteps() {
        return steps;
    }

    public long getMoves() {
        return moves;
    }

    public long getComparisons() {
        return comparisons;
    }

    public void reset() {
        steps = 0;
        moves = 0;
        comparisons = 0;
    }
}