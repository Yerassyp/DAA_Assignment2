package structures;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    @Test
    void getFromEmptyArrayThrowsException() {
        DynamicArray array = new DynamicArray();

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.get(0));
    }

    @Test
    void removeFromEmptyArrayThrowsException() {
        DynamicArray array = new DynamicArray();

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.remove(0));
    }

    @Test
    void addOneElement() {
        DynamicArray array = new DynamicArray();

        array.add(10);

        assertEquals(10, array.get(0));
    }

    @Test
    void containsDuplicateValues() {
        DynamicArray array = new DynamicArray();

        array.add(5);
        array.add(10);
        array.add(5);

        assertTrue(array.contains(5));
        assertTrue(array.contains(10));
        assertFalse(array.contains(20));
    }

    @Test
    void addAtFirstIndex() {
        DynamicArray array = new DynamicArray();

        array.add(10);
        array.add(20);
        array.add(0, 5);

        assertEquals(5, array.get(0));
        assertEquals(10, array.get(1));
        assertEquals(20, array.get(2));
    }

    @Test
    void addAtLastIndex() {
        DynamicArray array = new DynamicArray();

        array.add(10);
        array.add(20);
        array.add(2, 30);

        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
    }

    @Test
    void removeFirstIndex() {
        DynamicArray array = new DynamicArray();

        array.add(10);
        array.add(20);
        array.add(30);

        int removed = array.remove(0);

        assertEquals(10, removed);
        assertEquals(20, array.get(0));
        assertEquals(30, array.get(1));
    }

    @Test
    void removeLastIndex() {
        DynamicArray array = new DynamicArray();

        array.add(10);
        array.add(20);
        array.add(30);

        int removed = array.remove(2);

        assertEquals(30, removed);
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> array.get(2));
    }

    @Test
    void invalidGetIndexThrowsException() {
        DynamicArray array = new DynamicArray();

        array.add(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.get(-1));

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.get(1));
    }

    @Test
    void invalidAddIndexThrowsException() {
        DynamicArray array = new DynamicArray();

        array.add(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.add(-1, 20));

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.add(2, 20));
    }

    @Test
    void invalidRemoveIndexThrowsException() {
        DynamicArray array = new DynamicArray();

        array.add(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.remove(-1));

        assertThrows(IndexOutOfBoundsException.class,
                () -> array.remove(1));
    }

    @Test
    void growsWhenCapacityIsFull() {
        DynamicArray array = new DynamicArray();

        for (int i = 0; i < 25; i++) {
            array.add(i);
        }

        for (int i = 0; i < 25; i++) {
            assertEquals(i, array.get(i));
        }
    }

    @Test
    void matchesArrayListOnRandomOperations() {
        DynamicArray array = new DynamicArray();
        ArrayList<Integer> expected = new ArrayList<>();
        Random random = new Random(42);

        for (int i = 0; i < 1000; i++) {
            int value = random.nextInt(10_000);

            array.add(value);
            expected.add(value);
        }

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).intValue(), array.get(i));
        }

        for (int i = 0; i < 100; i++) {
            int index = random.nextInt(expected.size());
            int value = random.nextInt(10_000);

            array.add(index, value);
            expected.add(index, value);
        }

        for (int i = 0; i < 100; i++) {
            int index = random.nextInt(expected.size());

            int expectedRemoved = expected.remove(index);
            int actualRemoved = array.remove(index);

            assertEquals(expectedRemoved, actualRemoved);
        }

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).intValue(), array.get(i));
        }
    }
}