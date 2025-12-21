package com.example.pcdb.producer;

import com.example.pcdb.command.Command;
import com.example.pcdb.queue.BoundedQueue;

import java.util.List;
import java.util.Objects;

public final class CommandProducer implements Runnable {

    private final BoundedQueue<Command> queue;
    private final List<Command> commands;

    public CommandProducer(final BoundedQueue<Command> queue, final List<Command> commands) {
        this.queue = Objects.requireNonNull(queue, "Queue must not be null");
        this.commands = Objects.requireNonNull(commands, "Commands must not be null");
    }

    @Override
    public void run() {
        try {

            for (final var cmd : commands) {
                queue.put(cmd);
            }

        } catch (final InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
