package metrics;

import org.junit.jupiter.api.Test;
import structures.DynamicArray;
import structures.MinHeap;
import structures.MyLinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationMetricsTest {

    @Test
    void dynamicArrayGetCountsOneStep() {
        DynamicArray array = new DynamicArray();
        array.add(10);
        array.resetMetrics();

        assertEquals(10, array.get(0));

        assertMetrics(array.getMetrics(), 1, 0, 0);
    }

    @Test
    void dynamicArrayContainsCountsReadsAndComparisons() {
        DynamicArray array = new DynamicArray();
        array.add(10);
        array.add(20);
        array.add(30);
        array.resetMetrics();

        assertTrue(array.contains(30));

        assertMetrics(array.getMetrics(), 3, 0, 3);
    }

    @Test
    void dynamicArrayAddAtIndexCountsShifts() {
        DynamicArray array = new DynamicArray();
        array.add(10);
        array.add(20);
        array.add(30);
        array.resetMetrics();

        array.add(1, 99);

        assertMetrics(array.getMetrics(), 2, 2, 0);
    }

    @Test
    void dynamicArrayGrowCountsCopiedElements() {
        DynamicArray array = new DynamicArray();

        for (int i = 0; i < 10; i++) {
            array.add(i);
        }

        array.resetMetrics();

        array.add(10);

        assertMetrics(array.getMetrics(), 10, 10, 0);
    }

    @Test
    void linkedListGetCountsTraversalSteps() {
        MyLinkedList list = new MyLinkedList();
        list.add(10);
        list.add(20);
        list.add(30);
        list.resetMetrics();

        assertEquals(30, list.get(2));

        assertMetrics(list.getMetrics(), 2, 0, 0);
    }

    @Test
    void linkedListContainsCountsTraversalAndComparisons() {
        MyLinkedList list = new MyLinkedList();
        list.add(10);
        list.add(20);
        list.add(30);
        list.resetMetrics();

        assertTrue(list.contains(30));

        assertMetrics(list.getMetrics(), 2, 0, 3);
    }

    @Test
    void linkedListHeadInsertCountsPointerUpdates() {
        MyLinkedList list = new MyLinkedList();
        list.add(10);
        list.add(20);
        list.resetMetrics();

        list.add(0, 5);

        assertMetrics(list.getMetrics(), 0, 2, 0);
    }

    @Test
    void linkedListRemoveCountsPointerReadAndUpdate() {
        MyLinkedList list = new MyLinkedList();
        list.add(10);
        list.add(20);
        list.add(30);
        list.resetMetrics();

        assertEquals(20, list.remove(1));

        assertMetrics(list.getMetrics(), 2, 1, 0);
    }

    @Test
    void minHeapPeekCountsOneArrayRead() {
        MinHeap heap = new MinHeap();
        heap.insert(10);
        heap.resetMetrics();

        assertEquals(10, heap.peekMin());

        assertMetrics(heap.getMetrics(), 1, 0, 0);
    }

    @Test
    void minHeapBubbleUpCountsOperations() {
        MinHeap heap = new MinHeap();
        heap.insert(10);
        heap.insert(20);
        heap.insert(30);
        heap.resetMetrics();

        heap.insert(5);

        assertEquals(5, heap.peekMin());

        OperationMetrics metrics = heap.getMetrics();

        assertEquals(5, metrics.getSteps());
        assertEquals(4, metrics.getMoves());
        assertEquals(2, metrics.getComparisons());
    }

    @Test
    void minHeapExtractCountsBubbleDownOperations() {
        MinHeap heap = new MinHeap();
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        heap.insert(4);
        heap.resetMetrics();

        assertEquals(1, heap.extractMin());

        assertMetrics(heap.getMetrics(), 8, 3, 2);
    }

    @Test
    void resetClearsAllCounters() {
        DynamicArray array = new DynamicArray();

        array.add(10);
        array.add(20);
        array.resetMetrics();

        assertFalse(array.contains(30));

        assertTrue(array.getMetrics().getSteps() > 0);
        assertTrue(array.getMetrics().getComparisons() > 0);

        array.resetMetrics();

        assertMetrics(array.getMetrics(), 0, 0, 0);
    }

    private void assertMetrics(
            OperationMetrics metrics,
            long expectedSteps,
            long expectedMoves,
            long expectedComparisons) {

        assertEquals(expectedSteps, metrics.getSteps());
        assertEquals(expectedMoves, metrics.getMoves());
        assertEquals(expectedComparisons, metrics.getComparisons());
    }
}