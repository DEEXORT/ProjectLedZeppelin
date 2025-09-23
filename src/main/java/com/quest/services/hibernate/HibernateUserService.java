package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.User;
import com.quest.repository.RepositoryImpl;

import java.util.Collection;

public class HibernateUserService {
    private final RepositoryImpl<User> repository;

    public HibernateUserService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, User.class);
    }

    public void create(User user) {
        repository.create(user);
    }

    public Collection<User> getAll() {
        return repository.getAll();
    }

}
