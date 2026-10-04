package structures;

public class MinHeap {

    private int[] data;
    private int size;

    public MinHeap() {
        data = new int[10];
        size = 0;
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

        return data[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException();
        }

        int min = data[0];

        data[0] = data[size - 1];
        size--;

        if (size > 0) {
            bubbleDown(0);
        }

        return min;
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;

            if (data[parentIndex] <= data[index]) {
                break;
            }

            int temp = data[parentIndex];
            data[parentIndex] = data[index];
            data[index] = temp;

            index = parentIndex;
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            if (leftChild < size && data[leftChild] < data[smallest]) {
                smallest = leftChild;
            }

            if (rightChild < size && data[rightChild] < data[smallest]) {
                smallest = rightChild;
            }

            if (smallest == index) {
                break;
            }

            int temp = data[index];
            data[index] = data[smallest];
            data[smallest] = temp;

            index = smallest;
        }
    }

    private void grow() {
        int[] newData = new int[data.length * 2];

        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }

        data = newData;
    }
}