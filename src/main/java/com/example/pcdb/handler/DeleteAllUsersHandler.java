package com.example.pcdb.handler;

import com.example.pcdb.command.DeleteAllUsersCommand;
import com.example.pcdb.repository.UserRepository;

import java.util.Objects;

public final class DeleteAllUsersHandler implements CommandHandler<DeleteAllUsersCommand> {

    private final UserRepository repo;

    public DeleteAllUsersHandler(final UserRepository repo) {
        this.repo = Objects.requireNonNull(repo, "User repository must not be null");
    }

    @Override
    public Class<DeleteAllUsersCommand> type() {
        return DeleteAllUsersCommand.class;
    }

    @Override
    public void handle(final DeleteAllUsersCommand command) throws Exception {
        repo.deleteAll();
    }
}
