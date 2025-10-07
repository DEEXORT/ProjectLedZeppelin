package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.Achievement;
import com.quest.repository.RepositoryImpl;

import java.util.Collection;
import java.util.Optional;

public class HibernateAchievementService implements BaseService<Achievement> {
    private final RepositoryImpl<Achievement> repository;

    public HibernateAchievementService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, Achievement.class);
    }

    public HibernateAchievementService(RepositoryImpl<Achievement> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Achievement> get(long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Collection<Achievement> getAll() {
        return repository.getAll();
    }

    @Override
    public void create(Achievement achievement) {
        repository.create(achievement);
    }

    @Override
    public void update(Achievement achievement) {
        repository.update(achievement);
    }

    @Override
    public void delete(Achievement achievement) {
        repository.delete(achievement);
    }
}
