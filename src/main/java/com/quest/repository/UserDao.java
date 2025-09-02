package com.quest.repository;

import com.quest.config.ConnectionPool;
import com.quest.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDao implements Repository<User> {

    private ConnectionPool pool = new ConnectionPool(10);

    private final String SQL_GET_ALL = "SELECT id, login, password, player_id FROM users";
    private final String SQL_GET_BY_ID = "SELECT id, login, password, player_id FROM users WHERE id = ?";

    @Override
    public Collection<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_GET_ALL)) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public User get(long id) {
        try (Connection conn = pool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_GET_BY_ID)) {
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return buildUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void create(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(long id) {

    }

    private User buildUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .login(rs.getString("login"))
                .password(rs.getString("password"))
                .playerId(rs.getLong("playerId"))
                .build();
    }
}
