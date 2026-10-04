package structures;

import metrics.OperationMetrics;

public class MyLinkedList {

    private static class Node {
        private int value;
        private Node next;

        private Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private final OperationMetrics metrics;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
        metrics = new OperationMetrics();
    }

    public void add(int x) {
        Node newNode = new Node(x);

        if (head == null) {
            head = newNode;
            metrics.incrementMoves();

            tail = newNode;
            metrics.incrementMoves();
        } else {
            tail.next = newNode;
            metrics.incrementMoves();

            tail = newNode;
            metrics.incrementMoves();
        }

        size++;
    }

    public void add(int index, int x) {
        checkPositionIndex(index);

        if (index == size) {
            add(x);
            return;
        }

        Node newNode = new Node(x);

        if (index == 0) {
            newNode.next = head;
            metrics.incrementMoves();

            head = newNode;
            metrics.incrementMoves();

            size++;
            return;
        }

        Node current = head;

        for (int i = 0; i < index - 1; i++) {
            current = current.next;
            metrics.incrementSteps();
        }

        Node nextNode = current.next;
        metrics.incrementSteps();

        newNode.next = nextNode;
        metrics.incrementMoves();

        current.next = newNode;
        metrics.incrementMoves();

        size++;
    }

    public int remove(int index) {
        checkElementIndex(index);

        if (index == 0) {
            int removedValue = head.value;

            Node nextNode = head.next;
            metrics.incrementSteps();

            head = nextNode;
            metrics.incrementMoves();

            size--;

            if (size == 0) {
                tail = null;
                metrics.incrementMoves();
            }

            return removedValue;
        }

        Node current = head;

        for (int i = 0; i < index - 1; i++) {
            current = current.next;
            metrics.incrementSteps();
        }

        Node removedNode = current.next;
        metrics.incrementSteps();

        int removedValue = removedNode.value;

        Node nextNode = removedNode.next;
        metrics.incrementSteps();

        current.next = nextNode;
        metrics.incrementMoves();

        if (removedNode == tail) {
            tail = current;
            metrics.incrementMoves();
        }

        size--;

        return removedValue;
    }

    public int get(int index) {
        checkElementIndex(index);

        Node current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
            metrics.incrementSteps();
        }

        return current.value;
    }

    public boolean contains(int x) {
        Node current = head;

        while (current != null) {
            metrics.incrementComparisons();

            if (current.value == x) {
                return true;
            }

            current = current.next;
            metrics.incrementSteps();
        }

        return false;
    }

    public OperationMetrics getMetrics() {
        return metrics;
    }

    public void resetMetrics() {
        metrics.reset();
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