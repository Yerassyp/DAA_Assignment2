package structures;

import metrics.OperationMetrics;

public class DynamicArray {

    private int[] data;
    private int size;
    private final OperationMetrics metrics;

    public DynamicArray() {
        data = new int[10];
        size = 0;
        metrics = new OperationMetrics();
    }

    public void add(int x) {
        if (size == data.length) {
            grow();
        }

        data[size] = x;
        size++;
    }

    public void add(int index, int x) {
        checkPositionIndex(index);

        if (size == data.length) {
            grow();
        }

        for (int i = size; i > index; i--) {
            int value = data[i - 1];
            metrics.incrementSteps();

            data[i] = value;
            metrics.incrementMoves();
        }

        data[index] = x;
        size++;
    }

    public int remove(int index) {
        checkElementIndex(index);

        int removedValue = data[index];
        metrics.incrementSteps();

        for (int i = index; i < size - 1; i++) {
            int value = data[i + 1];
            metrics.incrementSteps();

            data[i] = value;
            metrics.incrementMoves();
        }

        size--;

        return removedValue;
    }

    public int get(int index) {
        checkElementIndex(index);

        int value = data[index];
        metrics.incrementSteps();

        return value;
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            int value = data[i];
            metrics.incrementSteps();

            metrics.incrementComparisons();

            if (value == x) {
                return true;
            }
        }

        return false;
    }

    public OperationMetrics getMetrics() {
        return metrics;
    }

    public void resetMetrics() {
        metrics.reset();
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

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void checkPositionIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
    }
}