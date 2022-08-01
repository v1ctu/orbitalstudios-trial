package me.v1ctu.util;

public class SqlConstants {

    public static final String CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS users (
            uuid VARCHAR(36) PRIMARY KEY,
            name VARCHAR(16),
            balance DOUBLE
        )
    """;

    public static final String REPLACE = """
        REPLACE INTO users VALUES (?, ?, ?)
    """;

    public static final String GET_BY_ID = """
        SELECT * FROM users WHERE uuid = ?
    """;

    public static final String GET_BY_NAME = """
        SELECT * FROM users WHERE name = ?
    """;

    public static final String FIND_ALL = """
        SELECT * FROM users
    """;

}
