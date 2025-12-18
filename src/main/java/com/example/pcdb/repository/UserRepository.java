package com.example.pcdb.repository;

import com.example.pcdb.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {

    void add(final User user) throws SQLException;

    List<User> findAll() throws SQLException;

    void deleteAll() throws SQLException;
}
