package com.quest.repository;

import com.quest.entity.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository implements Repository<User> {
    private final AtomicLong id = new AtomicLong();
    private final Map<Long, User> users = new ConcurrentHashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User get(long id) {
        return users.get(id);
    }

    @Override
    public void create(User user) {
        user.setId(id.incrementAndGet());
        update(user);
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    public User find(String login) {
        return users.values().stream().filter(user -> user.getLogin().equals(login)).findFirst().orElse(null);
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }
}
