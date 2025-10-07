package com.quest.repository;

import com.quest.entity.User;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@AllArgsConstructor
public class UserRepository {
    private final AtomicLong id = new AtomicLong();
    private final Map<Long, User> repository = new ConcurrentHashMap<>();
//    private final SessionCreator sessionCreator;

    public Collection<User> getAll() {
        return repository.values();
    }

    public User get(long id) {
        return repository.get(id);
    }

    public void create(User user) {
        user.setId(id.incrementAndGet());
        update(user);
        // ============For Hibernate=================
//        Session session = sessionCreator.getSession();
//        try (session) {
//            Transaction transaction = session.beginTransaction();
//            session.persist(user);
//            transaction.commit();
//        }
    }


    public void update(User user) {
        repository.put(user.getId(), user);
    }

    public User find(String login) {
        return repository.values().stream().filter(user -> user.getLogin().equals(login)).findFirst().orElse(null);
    }

    public void delete(long id) {
        repository.remove(id);
    }
}
