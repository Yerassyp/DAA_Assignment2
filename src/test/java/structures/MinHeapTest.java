package structures;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.PriorityQueue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {

    @Test
    void peekMinOnEmptyHeapThrowsException() {
        MinHeap heap = new MinHeap();

        assertThrows(IllegalStateException.class,
                heap::peekMin);
    }

    @Test
    void extractMinOnEmptyHeapThrowsException() {
        MinHeap heap = new MinHeap();

        assertThrows(IllegalStateException.class,
                heap::extractMin);
    }

    @Test
    void oneElementHeap() {
        MinHeap heap = new MinHeap();

        heap.insert(10);

        assertEquals(10, heap.peekMin());
        assertEquals(10, heap.extractMin());

        assertThrows(IllegalStateException.class,
                heap::peekMin);
    }

    @Test
    void handlesDuplicateValues() {
        MinHeap heap = new MinHeap();

        heap.insert(5);
        heap.insert(5);
        heap.insert(5);

        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
    }

    @Test
    void handlesNegativeValues() {
        MinHeap heap = new MinHeap();

        heap.insert(10);
        heap.insert(-5);
        heap.insert(0);
        heap.insert(-20);

        assertEquals(-20, heap.peekMin());
        assertEquals(-20, heap.extractMin());
        assertEquals(-5, heap.extractMin());
        assertEquals(0, heap.extractMin());
        assertEquals(10, heap.extractMin());
    }

    @Test
    void growsWhenCapacityIsFull() {
        MinHeap heap = new MinHeap();

        for (int i = 100; i >= 0; i--) {
            heap.insert(i);
        }

        for (int i = 0; i <= 100; i++) {
            assertEquals(i, heap.extractMin());
        }
    }

    @Test
    void heapPropertyHoldsAfterEveryInsert() throws Exception {
        MinHeap heap = new MinHeap();
        Random random = new Random(42);

        for (int i = 0; i < 1000; i++) {
            heap.insert(random.nextInt(10_000));

            assertHeapProperty(heap);
        }
    }

    @Test
    void heapPropertyHoldsAfterEveryExtractMin() throws Exception {
        MinHeap heap = new MinHeap();
        Random random = new Random(42);

        for (int i = 0; i < 1000; i++) {
            heap.insert(random.nextInt(10_000));
        }

        while (getHeapSize(heap) > 0) {
            heap.extractMin();

            assertHeapProperty(heap);
        }
    }

    @Test
    void extractMinReturnsSortedValues() {
        MinHeap heap = new MinHeap();
        Random random = new Random(42);

        for (int i = 0; i < 1000; i++) {
            heap.insert(random.nextInt(10_000));
        }

        int previous = Integer.MIN_VALUE;

        for (int i = 0; i < 1000; i++) {
            int current = heap.extractMin();

            assertTrue(current >= previous);

            previous = current;
        }
    }

    @Test
    void matchesPriorityQueueOnRandomData() {
        MinHeap heap = new MinHeap();
        PriorityQueue<Integer> expected = new PriorityQueue<>();
        Random random = new Random(42);

        for (int i = 0; i < 1000; i++) {
            int value = random.nextInt(20_001) - 10_000;

            heap.insert(value);
            expected.add(value);
        }

        while (!expected.isEmpty()) {
            assertEquals(expected.peek().intValue(), heap.peekMin());
            assertEquals(expected.poll().intValue(), heap.extractMin());
        }

        assertThrows(IllegalStateException.class,
                heap::extractMin);
    }

    private void assertHeapProperty(MinHeap heap) throws Exception {
        int[] data = getHeapData(heap);
        int size = getHeapSize(heap);

        for (int child = 1; child < size; child++) {
            int parent = (child - 1) / 2;

            assertTrue(
                    data[parent] <= data[child],
                    "Heap property violated at child index " + child
            );
        }
    }

    private int[] getHeapData(MinHeap heap) throws Exception {
        Field dataField = MinHeap.class.getDeclaredField("data");
        dataField.setAccessible(true);

        return (int[]) dataField.get(heap);
    }

    private int getHeapSize(MinHeap heap) throws Exception {
        Field sizeField = MinHeap.class.getDeclaredField("size");
        sizeField.setAccessible(true);

        return sizeField.getInt(heap);
    }
}