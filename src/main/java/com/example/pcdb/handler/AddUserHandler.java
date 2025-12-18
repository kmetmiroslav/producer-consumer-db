package com.example.pcdb.handler;

import com.example.pcdb.command.AddUserCommand;
import com.example.pcdb.repository.UserRepository;

import java.util.Optional;

public final class AddUserHandler implements CommandHandler<AddUserCommand> {

    private final UserRepository repo;

    public AddUserHandler(final UserRepository repo) {
        this.repo =
                Optional.ofNullable(repo)
                        .orElseThrow(() -> new IllegalArgumentException("repository must not be null"));
    }

    @Override
    public Class<AddUserCommand> type() {
        return AddUserCommand.class;
    }

    @Override
    public void handle(final AddUserCommand command) throws Exception {
        repo.add(command.user());
    }
}
