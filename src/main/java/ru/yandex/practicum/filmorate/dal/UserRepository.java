package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.rowmapper.UserResultSetExtractor;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class UserRepository extends BaseRepository<User> {

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, User.class);
    }

    private static final String FIND_ALL_QUERY = "SELECT u.user_id, u.email, u.login, u.name, u.birthday, " +
            "f.friend_id FROM users AS u " +
            "LEFT JOIN friends f ON f.user_id = u.user_id";
    private static final String FIND_BY_ID_QUERY = "SELECT u.user_id, u.email, u.login, u.name, u.birthday, " +
            "f.friend_id FROM users AS u " +
            "LEFT JOIN friends f ON f.user_id = u.user_id " +
            "WHERE u.user_id = ?";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT u.user_id, u.email, u.login, u.name, u.birthday, " +
            "f.friend_id FROM users AS u " +
            "LEFT JOIN friends f ON f.user_id = u.user_id " +
            "WHERE email = ?";
    private static final String FIND_BY_LOGIN_QUERY = "SELECT u.user_id, u.email, u.login, u.name, u.birthday, " +
            "f.friend_id FROM users AS u " +
            "LEFT JOIN friends f ON f.user_id = u.user_id " +
            "WHERE login = ?";
    private static final String FIND_FRIENDS_BY_ID_QUERY = "SELECT u.user_id, u.email, u.login, u.name, u.birthday, " +
            "f.friend_id FROM users AS u " +
            "LEFT JOIN friends f ON f.user_id = u.user_id " +
            "WHERE u.user_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String FIND_COMMON_FRIENDS_QUERY = "SELECT u.user_id, u.email, u.login, u.name, u.birthday, " +
            "f.friend_id FROM users AS u " +
            "LEFT JOIN friends f ON f.user_id = u.user_id " +
            "WHERE u.user_id IN (SELECT friend_id FROM friends WHERE user_id = ?" +
            "and friend_id in (SELECT friend_id FROM friends WHERE user_id = ?))";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? and friend_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, " +
            "login = ?, " +
            "name = ?, " +
            "birthday = ? " +
            "WHERE user_id = ?";
    private static final String INSERT_ADD_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_id) " +
            "VALUES (?, ?)";

    public Optional<User> findById(long userId) {
        return super.findBy(FIND_BY_ID_QUERY, new UserResultSetExtractor(), userId);
    }

    public Optional<User> findByEmail(String userEmail) {
        return super.findBy(FIND_BY_EMAIL_QUERY, new UserResultSetExtractor(), userEmail);
    }

    public Optional<User> findByLogin(String userLogin) {
        return super.findBy(FIND_BY_LOGIN_QUERY, new UserResultSetExtractor(), userLogin);
    }

    public List<User> findAll() {
        return super.findAll(FIND_ALL_QUERY, new UserResultSetExtractor());
    }

    public boolean delete(long userId) {
        return super.delete(DELETE_QUERY, userId);
    }

    public boolean deleteFriend(long userId, long friendId) {
        return super.delete(DELETE_FRIEND_QUERY, userId, friendId);
    }

    public User update(User user) {
        Long id = user.getId();
        super.update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public User save(User user) {
        Long id = super.insert(INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    public void addFriend(Long userId, Long friendId) {
        super.update(INSERT_ADD_FRIEND_QUERY, userId, friendId);
    }

    public List<User> viewFriends(long id) {
        return super.findAll(FIND_FRIENDS_BY_ID_QUERY, id);
    }

    public List<User> viewCommonFriends(long user1Id, long user2Id) {
        return super.findAll(FIND_COMMON_FRIENDS_QUERY, user1Id, user2Id);
    }
}