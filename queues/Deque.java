/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private int size;

    // construct an empty deque
    public Deque() {
        size = 0;
    }

    private class Node {
        Item item;
        Node previous;
        Node next;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = head;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item returnedItem = current.item;
            current = current.next;
            return returnedItem;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldHead = head;
        head = new Node();
        head.item = item;
        if (size >= 1) {
            head.next = oldHead;
            oldHead.previous = head;
        }
        else {
            tail = head;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldTail = tail;
        tail = new Node();
        tail.item = item;
        if (oldTail != null) {
            oldTail.next = tail;
            tail.previous = oldTail;
        }
        else {
            head = tail;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item returnedItem = head.item;
        if (size() > 1) {
            head = head.next;
            head.previous = null;
        }
        else {
            head = null;
            tail = null;
        }
        size--;
        return returnedItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item returnedItem = tail.item;
        if (size > 1) {
            Node newTail = tail.previous;
            newTail.next = null;
            tail = newTail;
        }
        else {
            tail = null;
            head = null;
        }
        size--;
        return returnedItem;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(5);
        deque.addLast(10);
        deque.addFirst(4);
        deque.addLast(11);
        deque.addFirst(2);
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
        System.out.println(deque.removeLast());
        System.out.println(deque.removeLast());
        for (int x : deque) {
            System.out.println(x);
        }
        System.out.println(deque.size());
        System.out.println(deque.isEmpty());
    }

}
