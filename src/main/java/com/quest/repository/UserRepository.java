package com.quest.repository;

import com.quest.entity.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository implements Repository<User> {
    private final AtomicLong id = new AtomicLong();
    private final Map<Long, User> repository = new ConcurrentHashMap<>();

    @Override
    public Collection<User> getAll() {
        return repository.values();
    }

    @Override
    public User get(long id) {
        return repository.get(id);
    }

    @Override
    public void create(User user) {
        user.setId(id.incrementAndGet());
        update(user);
    }

    @Override
    public void update(User user) {
        repository.put(user.getId(), user);
    }

    public User find(String login) {
        return repository.values().stream().filter(user -> user.getLogin().equals(login)).findFirst().orElse(null);
    }

    @Override
    public void delete(long id) {
        repository.remove(id);
    }
}
