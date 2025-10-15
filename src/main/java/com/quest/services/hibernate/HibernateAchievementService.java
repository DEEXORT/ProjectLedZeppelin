package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.dto.AchievementTo;
import com.quest.entity.Achievement;
import com.quest.mapping.Dto;
import com.quest.repository.RepositoryImpl;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class HibernateAchievementService {
    private RepositoryImpl<Achievement> repository;
    private Dto dto = Dto.MAPPER;

    public HibernateAchievementService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, Achievement.class);
    }

    public HibernateAchievementService(RepositoryImpl<Achievement> repository) {
        this.repository = repository;
    }

    public void create(AchievementTo achievementTo) {
        repository.create(dto.from(achievementTo));
    }

    public Collection<AchievementTo> getAll() {
        return repository.getAll().stream().map(dto::from).collect(Collectors.toList());
    }

    public void delete(AchievementTo achievementTo) {
        repository.delete(dto.from(achievementTo));
    }

    public Optional<AchievementTo> get(Long id) {
        return Optional.of(repository.get(id)).map(dto::from);
    }

    public void update(AchievementTo achievement) {
        repository.update(dto.from(achievement));
    }
}
