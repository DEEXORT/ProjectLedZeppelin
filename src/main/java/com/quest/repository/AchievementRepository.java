package com.quest.repository;

import com.quest.entity.Achievement;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AchievementRepository {
    private Map<Long, Achievement> repository = new ConcurrentHashMap<>();
    private static AtomicLong id = new AtomicLong();

    public Collection<Achievement> getAll() {
        return repository.values();
    }

    public Achievement get(long id) {
        return repository.get(id);
    }

    public void create(Achievement achievement) {
        achievement.setId(id.incrementAndGet());
        repository.put(achievement.getId(), achievement);
    }

    public void update(Achievement achievement) {
        repository.put(achievement.getId(), achievement);
    }

    public void delete(long id) {
        repository.remove(id);
    }
}
