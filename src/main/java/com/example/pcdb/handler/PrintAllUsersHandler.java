package com.example.pcdb.handler;

import com.example.pcdb.command.PrintAllUsersCommand;
import com.example.pcdb.model.User;
import com.example.pcdb.repository.UserRepository;

import java.io.PrintStream;
import java.util.Objects;

public final class PrintAllUsersHandler implements CommandHandler<PrintAllUsersCommand> {

    private final UserRepository repo;
    private final PrintStream out;

    public PrintAllUsersHandler(final UserRepository repo, final PrintStream out) {
        this.repo = Objects.requireNonNull(repo, "User repository must not be null");
        this.out = Objects.requireNonNull(out, "out must not be null");
    }

    @Override
    public Class<PrintAllUsersCommand> type() {
        return PrintAllUsersCommand.class;
    }

    @Override
    public void handle(final PrintAllUsersCommand command) throws Exception {
        final var users = repo.findAll();

        out.println("---- SUSERS LIST (" + users.size() + ") ----");
        for (final User u : users) {
            out.printf("USER_ID=%d, USER_GUID=%s, USER_NAME=%s%n",
                    u.userId(), u.userGuid(), u.userName());
        }
        out.println("------------------------");
    }
}
