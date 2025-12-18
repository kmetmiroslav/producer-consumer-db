package com.example.pcdb;

import com.example.pcdb.command.*;
import com.example.pcdb.consumer.CommandConsumer;
import com.example.pcdb.db.Database;
import com.example.pcdb.dispatch.CommandDispatcher;
import com.example.pcdb.handler.AddUserHandler;
import com.example.pcdb.handler.DeleteAllUsersHandler;
import com.example.pcdb.handler.PrintAllUsersHandler;
import com.example.pcdb.model.User;
import com.example.pcdb.producer.CommandProducer;
import com.example.pcdb.queue.BoundedQueue;
import com.example.pcdb.queue.SynchronizedBoundedQueue;
import com.example.pcdb.repository.JdbcUserRepository;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public final class DemoApp {

    private static final String JDBC_URL = "jdbc:h2:mem:demo;DB_CLOSE_DELAY=-1";

    public static void main(final String[] args) throws Exception {

        //init database
        final Database db = new Database(JDBC_URL);
        db.initSchema();
        final var userRepo = new JdbcUserRepository(db);

        //init dispatcher
        final var dispatcher =
                new CommandDispatcher
                        (List.of(
                                new AddUserHandler(userRepo),
                                new PrintAllUsersHandler(userRepo, System.out),
                                new DeleteAllUsersHandler(userRepo)
                                )
                        );

        //init queue
        final BoundedQueue<Command> queue = new SynchronizedBoundedQueue<>(1);

        //prepare commands to run
        final List<Command> commands = List.of(
                new AddUserCommand(new User(1, "a1", "Robert")),
                new AddUserCommand(new User(2, "a2", "Martin")),
                new PrintAllUsersCommand(),
                new DeleteAllUsersCommand(),
                new PrintAllUsersCommand(),
                new ShutdownCommand()
        );

        //prepare producers and consumer
        final AtomicBoolean keepRunning = new AtomicBoolean(true);

        try (var exec =
                     Executors.newThreadPerTaskExecutor(
                             Thread
                                     .ofVirtual()
                                     .name("pc-vt-", 0)
                                     .factory()
                     )
        ) {
            final Future<?> consumer = exec.submit(new CommandConsumer(queue, dispatcher, keepRunning::get));
            final Future<?> producer = exec.submit(new CommandProducer(queue, commands));

            producer.get();
            consumer.get();

            //explicit stop for consumer
            // keepRunning.set(false);
            // consumer.cancel(true);
        }
    }
}
