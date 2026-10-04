package structures;

import metrics.OperationMetrics;

public class MinHeap {

    private int[] data;
    private int size;
    private final OperationMetrics metrics;

    public MinHeap() {
        data = new int[10];
        size = 0;
        metrics = new OperationMetrics();
    }

    public void insert(int x) {
        if (size == data.length) {
            grow();
        }

        data[size] = x;
        bubbleUp(size);
        size++;
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException();
        }

        int value = data[0];
        metrics.incrementSteps();

        return value;
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException();
        }

        int min = data[0];
        metrics.incrementSteps();

        int lastValue = data[size - 1];
        metrics.incrementSteps();

        data[0] = lastValue;
        metrics.incrementMoves();

        size--;

        if (size > 0) {
            bubbleDown(0);
        }

        return min;
    }

    public OperationMetrics getMetrics() {
        return metrics;
    }

    public void resetMetrics() {
        metrics.reset();
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;

            int parentValue = data[parentIndex];
            metrics.incrementSteps();

            int currentValue = data[index];
            metrics.incrementSteps();

            metrics.incrementComparisons();

            if (parentValue <= currentValue) {
                break;
            }

            data[parentIndex] = currentValue;
            metrics.incrementMoves();

            data[index] = parentValue;
            metrics.incrementMoves();

            index = parentIndex;
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            if (leftChild < size) {
                int leftValue = data[leftChild];
                metrics.incrementSteps();

                int smallestValue = data[smallest];
                metrics.incrementSteps();

                metrics.incrementComparisons();

                if (leftValue < smallestValue) {
                    smallest = leftChild;
                }
            }

            if (rightChild < size) {
                int rightValue = data[rightChild];
                metrics.incrementSteps();

                int smallestValue = data[smallest];
                metrics.incrementSteps();

                metrics.incrementComparisons();

                if (rightValue < smallestValue) {
                    smallest = rightChild;
                }
            }

            if (smallest == index) {
                break;
            }

            int currentValue = data[index];
            metrics.incrementSteps();

            int smallestValue = data[smallest];
            metrics.incrementSteps();

            data[index] = smallestValue;
            metrics.incrementMoves();

            data[smallest] = currentValue;
            metrics.incrementMoves();

            index = smallest;
        }
    }

    private void grow() {
        int[] newData = new int[data.length * 2];

        for (int i = 0; i < size; i++) {
            int value = data[i];
            metrics.incrementSteps();

            newData[i] = value;
            metrics.incrementMoves();
        }

        data = newData;
    }
}