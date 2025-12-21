package com.example.pcdb.model;

import java.util.Objects;

public record User(long userId, String userGuid, String userName) {

    public User {
        Objects.requireNonNull(userGuid, "User GUID must not be null");
        Objects.requireNonNull(userName, "User name must not be null");
    }
}
