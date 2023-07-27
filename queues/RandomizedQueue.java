/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] queue;
    // Stores index that were made null, to be filled during enqueue()
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
        n = 0;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int index = 0;
        private Item[] queueIterator;

        public RandomizedQueueIterator() {
            queueIterator = createQueue(size(), size());
            StdRandom.shuffle(queueIterator);
        }

        public boolean hasNext() {
            return index < queueIterator.length;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item returnedItem = queueIterator[index];
            index++;
            return returnedItem;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    private void resize(int capacity, int limit) {
        queue = createQueue(capacity, limit);
    }

    private Item[] createQueue(int capacity, int limit) {
        Item[] newQueue = (Item[]) new Object[capacity];
        for (int i = 0; i < limit; i++) {
            newQueue[i] = queue[i];
        }
        return newQueue;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (n == queue.length) {
            resize(queue.length * 2, size());
        }
        queue[n++] = item;
    }

    private int getRandomIndex() {
        int randomIndex = StdRandom.uniformInt(n);
        return randomIndex;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int randomIndex = getRandomIndex();
        Item returnedItem = queue[randomIndex];
        if (size() == 1) {
            queue[randomIndex] = null;
        }
        else {
            int lastItemIndex = n - 1;
            queue[randomIndex] = queue[lastItemIndex];
            queue[lastItemIndex] = null;

        }
        n--;
        if (n > 0 && n == queue.length / 4) {
            resize(queue.length / 2, size());
        }
        return returnedItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int randomIndex = getRandomIndex();
        Item returnedItem = queue[randomIndex];
        return returnedItem;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> r = new RandomizedQueue<Integer>();
        System.out.println("Enqueue");
        for (int i = 0; i < 50; i++) {
            r.enqueue(i);
        }
        System.out.println();
        System.out.println("Size");
        System.out.println(r.size());
        System.out.println();
        System.out.println("Dequeue");
        for (int i = 0; i < 50; i++) {
            System.out.println(r.dequeue());
        }
        System.out.println();
        System.out.println("Iterator");
        for (int x : r) {
            System.out.println(x);
        }
        System.out.println(r.isEmpty());
        System.out.println(r.sample());

    }

}
