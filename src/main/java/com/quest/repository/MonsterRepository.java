package com.quest.repository;

import com.quest.entity.Monster;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MonsterRepository implements Repository<Monster> {
    private final Map<Long, Monster> repository = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong();

    @Override
    public Collection<Monster> getAll() {
        return repository.values();
    }

    @Override
    public Monster get(long id) {
        return repository.get(id);
    }

    @Override
    public void create(Monster monster) {
        monster.setId(id.incrementAndGet());
        repository.put(monster.getId(), monster);
    }

    @Override
    public void update(Monster monster) {
        repository.put(monster.getId(), monster);
    }

    @Override
    public void delete(long id) {
        repository.remove(id);
    }

    public Monster find(String name) {
        return repository.values().stream()
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
