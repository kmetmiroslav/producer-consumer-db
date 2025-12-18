package com.example.pcdb.command;

public sealed interface Command
        permits AddUserCommand, PrintAllUsersCommand, DeleteAllUsersCommand, ShutdownCommand {
}
