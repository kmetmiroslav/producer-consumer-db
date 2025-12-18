package com.example.pcdb.model;

import java.util.Optional;

public record User(long userId, String userGuid, String userName) {

    public User {

        Optional.ofNullable(userGuid)
                .orElseThrow(() -> new IllegalArgumentException("User GUID must not be null"));

        Optional.ofNullable(userName)
                .orElseThrow(() -> new IllegalArgumentException("User name must not be null"));
    }
}
