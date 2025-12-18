package com.example.pcdb.queue;

public interface BoundedQueue<T> {

    void put(final T item) throws InterruptedException;

    T take() throws InterruptedException;

    int capacity();

    int size();
}
