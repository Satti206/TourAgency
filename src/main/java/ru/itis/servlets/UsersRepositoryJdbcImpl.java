package ru.itis.servlets;

import ru.itis.models.User;
import ru.itis.repository.UsersRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    private Connection connection;
    private static final String SQL_INSERT_INTO_USERS = "INSERT INTO users (email, password, name, lastname) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_LOGIN = "SELECT * FROM users WHERE email = ? AND password = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM users WHERE id = ?";

    public UsersRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<User> findAll() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = statement.executeQuery()) {

            List<User> result = new ArrayList<>();
            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getLong("id"))
                        .email(resultSet.getString("email"))
                        .password(resultSet.getString("password"))
                        .name(resultSet.getString("name"))
                        .lastname(resultSet.getString("lastname"))
                        .build();
                result.add(user);
            }

            return result;

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(User entity) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_INTO_USERS,
                PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getName());
            statement.setString(4, entity.getLastname());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(User user) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_LOGIN)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(User.builder()
                            .id(resultSet.getLong("id"))
                            .email(resultSet.getString("email"))
                            .password(resultSet.getString("password"))
                            .name(resultSet.getString("name"))
                            .lastname(resultSet.getString("lastname"))
                            .build());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> getById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(User.builder()
                            .id(resultSet.getLong("id"))
                            .email(resultSet.getString("email"))
                            .password(resultSet.getString("password"))
                            .name(resultSet.getString("name"))
                            .lastname(resultSet.getString("lastname"))
                            .build());
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }
    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}