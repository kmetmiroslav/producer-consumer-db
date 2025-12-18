package com.example.pcdb.dispatch;

import com.example.pcdb.command.Command;
import com.example.pcdb.handler.CommandHandler;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Dispatches commands to handlers based on the command runtime class.
 */
public final class CommandDispatcher {

    private final Map<Class<? extends Command>, CommandHandler<?>> handlersByType;

    public CommandDispatcher(final List<CommandHandler<?>> handlers) {

        Optional.ofNullable(handlers)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("Command handlers must not be null or empty"));

        this.handlersByType =
                handlers
                        .stream()
                        .collect(
                                Collectors.toUnmodifiableMap(
                                        CommandHandler::type,
                                        Function.identity(),
                                        (a, b) -> { throw new IllegalArgumentException("Duplicate handler for type: " + a.type().getName());
                                }
                        ));
    }

    public void dispatch(final Command command) throws Exception {
        Optional.ofNullable(command)
                .orElseThrow(() -> new IllegalArgumentException("Command must not be null"));

        final var handler =
                Optional.ofNullable(handlersByType.get(command.getClass()))
                        .orElseThrow(() -> new IllegalArgumentException("No handler registered for: " + command.getClass().getName()));

        dispatchUnsafe(command, handler);
    }

    @SuppressWarnings("unchecked")
    private <C extends Command> void dispatchUnsafe(final Command command, final CommandHandler<?> handler) throws Exception {
        final CommandHandler<C> typed = (CommandHandler<C>) handler;
        typed.handle((C) command);
    }
}
