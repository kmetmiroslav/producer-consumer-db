package com.example.pcdb.repository;

import com.example.pcdb.db.Database;
import com.example.pcdb.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class JdbcUserRepository implements UserRepository {

    private static final String SQL_USER_ADD = "INSERT INTO susers (user_id, user_guid, user_name) VALUES (?, ?, ?)";
    private static final String SQL_USER_FETCH_ALL = "SELECT user_id, user_guid, user_name FROM susers ORDER BY user_id";
    private static final String SQL_USER_DELETE_ALL = "DELETE FROM SUSERS";

    private final Database db;

    public JdbcUserRepository(final Database db) {
        this.db =
                Optional.ofNullable(db)
                        .orElseThrow(() -> new IllegalArgumentException("Database must not be null"));
    }

    @Override
    public void add(final User user) throws SQLException {
        Optional.ofNullable(user)
                .orElseThrow(() -> new IllegalArgumentException("User must not be null"));

        try (final var conn = db.getConnection();
             final var ps = conn.prepareStatement(SQL_USER_ADD))
        {
            ps.setLong(1, user.userId());
            ps.setString(2, user.userGuid());
            ps.setString(3, user.userName());
            ps.executeUpdate();
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        final List<User> users = new ArrayList<>();

        try (final var conn = db.getConnection();
             final var ps = conn.prepareStatement(SQL_USER_FETCH_ALL);
             final var rs = ps.executeQuery())
        {
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("USER_ID"),
                        rs.getString("USER_GUID"),
                        rs.getString("USER_NAME")
                ));
            }
        }
        return users;
    }

    @Override
    public void deleteAll() throws SQLException {
        try (final var conn = db.getConnection();
             final var st = conn.createStatement())
        {
            st.executeUpdate(SQL_USER_DELETE_ALL);
        }
    }
}
