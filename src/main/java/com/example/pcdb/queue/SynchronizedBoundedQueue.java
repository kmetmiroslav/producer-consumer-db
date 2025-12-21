package com.example.pcdb.queue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * <p>Class representing FIFO queue using Deque internally.</p>
 *
 * @param <T>
 */
public final class SynchronizedBoundedQueue<T> implements BoundedQueue<T> {

    private final Object lock = new Object();
    private final int capacity;
    private final Deque<T> deck;

    public SynchronizedBoundedQueue(final int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Queue capacity must be at least 1");
        }

        this.capacity = capacity;
        this.deck = new ArrayDeque<>(capacity);
    }

    @Override
    public void put(final T item) throws InterruptedException {
        Objects.requireNonNull(item, "Item must not be null");

        synchronized (lock) {

            while (deck.size() >= capacity) {
                lock.wait();
            }

            deck.addLast(item);
            lock.notifyAll();
        }
    }

    @Override
    public T take() throws InterruptedException {
        synchronized (lock) {

            while (deck.isEmpty()) {
                lock.wait();
            }

            final T item = deck.removeFirst();
            lock.notifyAll();

            return item;
        }
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public int size() {
        synchronized (lock) {
            return deck.size();
        }
    }
}
