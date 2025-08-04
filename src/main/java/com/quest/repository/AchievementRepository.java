package com.quest.repository;

import com.quest.entity.Achievement;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AchievementRepository implements Repository<Achievement> {
    private Map<Long, Achievement> repository = new ConcurrentHashMap<>();
    private static AtomicLong id = new AtomicLong();

    @Override
    public Collection<Achievement> getAll() {
        return repository.values();
    }

    @Override
    public Achievement get(long id) {
        return repository.get(id);
    }

    @Override
    public void create(Achievement achievement) {
        achievement.setId(id.incrementAndGet());
        repository.put(achievement.getId(), achievement);
    }

    @Override
    public void update(Achievement achievement) {
        repository.put(achievement.getId(), achievement);
    }

    @Override
    public void delete(long id) {
        repository.remove(id);
    }
}
