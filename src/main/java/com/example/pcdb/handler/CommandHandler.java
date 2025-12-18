package com.example.pcdb.handler;

import com.example.pcdb.command.Command;

public interface CommandHandler<C extends Command> {

    Class<C> type();

    void handle(final C command) throws Exception;
}
