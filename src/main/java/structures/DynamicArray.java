package structures;

public class DynamicArray {

    private int[] data;
    private int size;

    public DynamicArray() {
        data = new int[10];
        size = 0;
    }

    public void add(int x) {
        if (size == data.length) {
            grow();
        }

        data[size] = x;
        size++;
    }

    private void grow() {
        int[] newData = new int[data.length * 2];

        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }

        data = newData;
    }
}