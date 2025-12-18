package com.example.pcdb.repository;

import com.example.pcdb.db.Database;
import com.example.pcdb.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcUserRepositoryTest {

    private Database db;
    private UserRepository repo;

    @BeforeEach
    void setUp() throws SQLException {
        db = new Database("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        db.initSchema();
        repo = new JdbcUserRepository(db);
        repo.deleteAll();
    }

    @Test
    void shouldAddUsers() throws Exception {
        repo.add(new User(1, "a1", "Robert"));
        repo.add(new User(2, "a2", "Martin"));

        List<User> users = repo.findAll();
        assertEquals(2, users.size());
        assertEquals(1, users.get(0).userId());
        assertEquals("Robert", users.get(0).userName());
    }

    @Test
    void shouldFindAllUsers() throws Exception {
        repo.add(new User(1, "a1", "Robert"));
        repo.add(new User(2, "a2", "Martin"));

        List<User> users = repo.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void shouldDeleteAllUsers() throws Exception {
        repo.add(new User(1, "a1", "Robert"));
        repo.add(new User(2, "a2", "Martin"));

        List<User> users = repo.findAll();
        assertEquals(2, users.size());

        repo.deleteAll();
        assertTrue(repo.findAll().isEmpty());
    }

}
