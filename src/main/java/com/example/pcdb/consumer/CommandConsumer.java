package com.example.pcdb.consumer;

import com.example.pcdb.command.Command;
import com.example.pcdb.command.ShutdownCommand;
import com.example.pcdb.dispatch.CommandDispatcher;
import com.example.pcdb.queue.BoundedQueue;

import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * Command consumer that takes items from a bounded FIFO queue and dispatches them for processing.
 *
 * Stop options:
 *  - Graceful: add a ShutdownCommand to the queue
 *  - External: set the passed in BooleanSupplier to value false and interrupt the thread using Future.cancel(true)
 */
public final class CommandConsumer implements Runnable {

    private final BoundedQueue<Command> queue;
    private final CommandDispatcher dispatcher;
    private final BooleanSupplier keepRunning;

    public CommandConsumer(final BoundedQueue<Command> queue,
                           final CommandDispatcher dispatcher)
    {
        this(queue, dispatcher, () -> true);
    }

    public CommandConsumer(final BoundedQueue<Command> queue,
                           final CommandDispatcher dispatcher,
                           final BooleanSupplier keepRunning)
    {
        this.queue =
                Optional.ofNullable(queue)
                        .orElseThrow(() -> new IllegalArgumentException("Queue must not be null"));

        this.dispatcher =
                Optional.ofNullable(dispatcher)
                        .orElseThrow(() -> new IllegalArgumentException("Dispatcher must not be null"));

        this.keepRunning =
                Optional.ofNullable(keepRunning)
                        .orElseThrow(() -> new IllegalArgumentException("KeepRunning flag supplier must not be null"));
    }

    @Override
    public void run() {
        try {

            while (keepRunning.getAsBoolean() && !Thread.currentThread().isInterrupted()) {

                final var item = queue.take();
                if (item instanceof ShutdownCommand) {
                    return;
                }

                try {
                    dispatcher.dispatch(item);
                } catch (final Exception e) {
                    e.printStackTrace(System.err);
                }
            }

        } catch (final InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
