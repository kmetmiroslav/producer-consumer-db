package com.example.pcdb.queue;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class SynchronizedBoundedQueueTest {

    @Test
    void shouldPreserveFifoOrder() throws Exception {
        BoundedQueue<Integer> q = new SynchronizedBoundedQueue<>(3);
        q.put(1);
        q.put(2);
        q.put(3);

        assertEquals(1, q.take());
        assertEquals(2, q.take());
        assertEquals(3, q.take());
    }

    @Test
    void shouldBlockWhenFullUntilTakeHappens() throws Exception {
        BoundedQueue<Integer> q = new SynchronizedBoundedQueue<>(1);
        q.put(1); // full

        AtomicBoolean secondPutCompleted = new AtomicBoolean(false);

        Thread t = new Thread(() -> {
            try {
                q.put(2);
                secondPutCompleted.set(true);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        });

        t.start();

        Thread.sleep(50);
        assertFalse(secondPutCompleted.get());

        assertEquals(1, q.take());

        t.join(500);
        assertTrue(secondPutCompleted.get());

        assertEquals(2, q.take());
    }
}
