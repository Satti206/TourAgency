package ru.itis.repository;

import ru.itis.models.Tour;
import ru.itis.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    List<User> findAll();
    void save(User entity);
    Optional<User> findByEmailAndRole(User user);
    Optional<User> getById(long id);
    Optional<User> findById(long id) throws SQLException;//вот тут добавила throws
    void close() throws SQLException;
    List<Tour> findAllTours();
    void updateProfile(User user) throws SQLException;

    void updateAvatar(long userId, byte[] avatar) throws SQLException;

}
