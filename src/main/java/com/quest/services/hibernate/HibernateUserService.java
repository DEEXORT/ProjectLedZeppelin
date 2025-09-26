package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.User;
import com.quest.repository.RepositoryImpl;

import java.util.Collection;
import java.util.Optional;

public class HibernateUserService implements BaseService<User> {
    private final RepositoryImpl<User> repository;

    public HibernateUserService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, User.class);
    }

    public HibernateUserService(RepositoryImpl<User> repository) {
        this.repository = repository;
    }

    @Override
    public void create(User user) {
        repository.create(user);
    }

    @Override
    public Collection<User> getAll() {
        return repository.getAll();
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public void update(User entity) {
        repository.update(entity);
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }
}
