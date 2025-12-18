package com.example.pcdb.command;

import com.example.pcdb.model.User;

import java.util.Optional;

public record AddUserCommand(User user) implements Command {

    public AddUserCommand {
        Optional.ofNullable(user)
                .orElseThrow(() -> new IllegalArgumentException("User must not be null"));
    }
}
