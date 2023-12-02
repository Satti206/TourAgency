package ru.itis.servlets;

import ru.itis.models.Tour;
import ru.itis.models.User;
import ru.itis.repository.UsersRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    private Connection connection;
    private static final String SQL_INSERT_INTO_USERS = "INSERT INTO users (email, password, name, lastname, role)" +
            " VALUES (?, ?, ?, ?, ?)";
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
                        .role(resultSet.getString("role"))
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
            statement.setString(5,entity.getRole());

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
    public Optional<User> findByEmailAndRole(User user) {
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
                            .role(resultSet.getString("role"))
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
    public Optional<User> findById(long id) {
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

    @Override
    public List<Tour> findAllTours() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM tours");
             ResultSet resultSet = statement.executeQuery()) {

            List<Tour> result = new ArrayList<>();
            while (resultSet.next()) {
                Tour tour = Tour.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .price(resultSet.getInt("price"))
                        .direction(resultSet.getString("direction"))
                        .country(resultSet.getString("country"))
                        .town(resultSet.getString("town"))
                        .startDate(resultSet.getString("startDate"))
                        .endDate(resultSet.getString("endDate"))
                        .build();
                result.add(tour);
            }

            return result;

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
    @Override
    public void updateProfile(User user) throws SQLException {
        String query = "UPDATE users SET name = ?, lastname = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getLastname());
            statement.setLong(3, user.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void updateAvatar(long userId, byte[] avatar) throws SQLException {
        String query = "UPDATE users SET avatar = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBytes(1, avatar);
            statement.setLong(2, userId);
            statement.executeUpdate();
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .lastname(resultSet.getString("lastname"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .role(resultSet.getString("role"))
                .profilePicturePath(resultSet.getString("profile_picture_path"))
                .avatar(resultSet.getBytes("avatar"))
                .build();
    }
}

