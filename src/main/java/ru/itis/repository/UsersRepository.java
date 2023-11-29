package ru.itis.repository;

import ru.itis.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    List<User> findAll();
    void save(User entity);
    Optional<User> findByEmail(User user);
    Optional<User> getById(long id);
    void close() throws SQLException;
}
