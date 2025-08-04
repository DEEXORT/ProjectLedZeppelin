package com.quest.services;

import com.quest.entity.Achievement;
import com.quest.repository.AchievementRepository;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class AchievementService {
    private final AchievementRepository repository;

    public void create(Achievement achievement) {
        repository.create(achievement);
    }

    public void get(long id) {
        repository.get(id);
    }

    public Collection<Achievement> getAll() {
        return repository.getAll();
    }

    public void update(Achievement achievement) {
        repository.update(achievement);
    }

    public void delete(long id) {
        repository.delete(id);
    }
}
