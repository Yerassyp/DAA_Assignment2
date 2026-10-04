package structures;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {

    @Test
    void getFromEmptyListThrowsException() {
        MyLinkedList list = new MyLinkedList();

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(0));
    }

    @Test
    void removeFromEmptyListThrowsException() {
        MyLinkedList list = new MyLinkedList();

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.remove(0));
    }

    @Test
    void addOneElement() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);

        assertEquals(10, list.get(0));
    }

    @Test
    void containsDuplicateValues() {
        MyLinkedList list = new MyLinkedList();

        list.add(5);
        list.add(10);
        list.add(5);

        assertTrue(list.contains(5));
        assertTrue(list.contains(10));
        assertFalse(list.contains(20));
    }

    @Test
    void addAtFirstIndex() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);
        list.add(20);
        list.add(0, 5);

        assertEquals(5, list.get(0));
        assertEquals(10, list.get(1));
        assertEquals(20, list.get(2));
    }

    @Test
    void addAtLastIndex() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);
        list.add(20);
        list.add(2, 30);

        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void addAtMiddleIndex() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);
        list.add(30);
        list.add(1, 20);

        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void removeFirstIndex() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);
        list.add(20);
        list.add(30);

        int removed = list.remove(0);

        assertEquals(10, removed);
        assertEquals(20, list.get(0));
        assertEquals(30, list.get(1));
    }

    @Test
    void removeLastIndex() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);
        list.add(20);
        list.add(30);

        int removed = list.remove(2);

        assertEquals(30, removed);
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(2));
    }

    @Test
    void removeOnlyElementAndAddAgain() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);

        assertEquals(10, list.remove(0));

        list.add(20);

        assertEquals(20, list.get(0));
    }

    @Test
    void invalidGetIndexThrowsException() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(-1));

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.get(1));
    }

    @Test
    void invalidAddIndexThrowsException() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(-1, 20));

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.add(2, 20));
    }

    @Test
    void invalidRemoveIndexThrowsException() {
        MyLinkedList list = new MyLinkedList();

        list.add(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.remove(-1));

        assertThrows(IndexOutOfBoundsException.class,
                () -> list.remove(1));
    }

    @Test
    void matchesArrayListOnRandomOperations() {
        MyLinkedList list = new MyLinkedList();
        ArrayList<Integer> expected = new ArrayList<>();
        Random random = new Random(42);

        for (int i = 0; i < 1000; i++) {
            int value = random.nextInt(10_000);

            list.add(value);
            expected.add(value);
        }

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).intValue(), list.get(i));
        }

        for (int i = 0; i < 100; i++) {
            int index = random.nextInt(expected.size() + 1);
            int value = random.nextInt(10_000);

            list.add(index, value);
            expected.add(index, value);
        }

        for (int i = 0; i < 100; i++) {
            int index = random.nextInt(expected.size());

            int expectedRemoved = expected.remove(index);
            int actualRemoved = list.remove(index);

            assertEquals(expectedRemoved, actualRemoved);
        }

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).intValue(), list.get(i));
        }
    }
}