package me.v1ctu.user;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static me.v1ctu.util.SqlConstants.*;

public class UserRepository {

    private final HikariDataSource hikariDataSource;

    public UserRepository(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
        try (final Connection connection = hikariDataSource.getConnection()) {
            connection.createStatement().executeUpdate(CREATE_TABLE);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void replace(User user) {
        try (final Connection connection = hikariDataSource.getConnection()) {
            final PreparedStatement preparedStatement = connection.prepareStatement(REPLACE);
            preparedStatement.setString(1, user.getUuid().toString());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setDouble(3, user.getBalance());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public User getById(UUID uuid) {
        try (final Connection connection = hikariDataSource.getConnection()) {
            final PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
            preparedStatement.setString(1, uuid.toString());
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return null;
            return new User(
                UUID.fromString(resultSet.getString("uuid")),
                resultSet.getString("name"),
                resultSet.getDouble("balance")
            );
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public User getByName(String name) {
        try (final Connection connection = hikariDataSource.getConnection()) {
            final PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_NAME);
            preparedStatement.setString(1, name);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return null;
            return new User(
                UUID.fromString(resultSet.getString("uuid")),
                resultSet.getString("name"),
                resultSet.getDouble("balance")
            );
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }



}
