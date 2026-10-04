package structures;

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

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void add(int x) {
        Node newNode = new Node(x);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
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
            head = newNode;
            size++;
            return;
        }

        Node current = head;

        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }

        newNode.next = current.next;
        current.next = newNode;

        size++;
    }

    public int remove(int index) {
        checkElementIndex(index);

        if (index == 0) {
            int removedValue = head.value;

            head = head.next;
            size--;

            if (size == 0) {
                tail = null;
            }

            return removedValue;
        }

        Node current = head;

        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }

        Node removedNode = current.next;
        int removedValue = removedNode.value;

        current.next = removedNode.next;

        if (removedNode == tail) {
            tail = current;
        }

        size--;

        return removedValue;
    }

    public int get(int index) {
        checkElementIndex(index);

        Node current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.value;
    }

    public boolean contains(int x) {
        Node current = head;

        while (current != null) {
            if (current.value == x) {
                return true;
            }

            current = current.next;
        }

        return false;
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