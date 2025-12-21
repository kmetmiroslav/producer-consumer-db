package com.example.pcdb.command;

import com.example.pcdb.model.User;

import java.util.Objects;

public record AddUserCommand(User user) implements Command {

    public AddUserCommand {
        Objects.requireNonNull(user, "User must not be null");
    }
}
