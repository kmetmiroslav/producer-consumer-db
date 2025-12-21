package com.example.pcdb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public final class Database {

    private static final String DDL_SUSERS_TABLE =
            """
            CREATE TABLE IF NOT EXISTS susers (
              user_id   BIGINT PRIMARY KEY,
              user_guid VARCHAR(64) NOT NULL,
              user_name VARCHAR(255) NOT NULL
            )
            """;

    private final String jdbcUrl;

    public Database(final String jdbcUrl) {
        this.jdbcUrl = Objects.requireNonNull(jdbcUrl, "JDBC url must not be null");
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }

    public void initSchema() throws SQLException {
        try (final var conn = getConnection();
             final var st = conn.createStatement())
        {
            st.execute(DDL_SUSERS_TABLE);
        }
    }
}
