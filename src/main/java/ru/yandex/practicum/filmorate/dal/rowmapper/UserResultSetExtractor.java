package ru.yandex.practicum.filmorate.dal.rowmapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserResultSetExtractor implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, User> userMap = new LinkedHashMap<>();

        while (rs.next()) {
            Long userId = rs.getLong("user_id");

            User user = userMap.get(userId);
            if (user == null) {
                user = new User();
                user.setId(userId);
                user.setEmail(rs.getString("email"));
                user.setLogin(rs.getString("login"));
                user.setName(rs.getString("name"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());

                HashSet<Long> friends = new HashSet<>();
                user.setFriends(friends);

                userMap.put(userId, user);
            }

            Long friendId = rs.getLong("friend_id");
            if (!rs.wasNull() && friendId != 0) {
                user.getFriends().add(friendId);
            }
        }

        return new ArrayList<>(userMap.values());
    }
}

