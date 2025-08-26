package com.quest.repository;

import com.quest.entity.Ability;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AbilityRepository implements Repository<Ability>{
    private final Map<Long, Ability> repository = new ConcurrentHashMap<>();
    private static final AtomicLong id = new AtomicLong();

    @Override
    public Collection<Ability> getAll() {
        return repository.values();
    }

    @Override
    public Ability get(long id) {
        return repository.get(id);
    }

    @Override
    public void create(Ability ability) {
        ability.setId(id.incrementAndGet());
        update(ability);
    }

    @Override
    public void update(Ability ability) {
        repository.put(ability.getId(), ability);
    }

    @Override
    public void delete(long id) {
        repository.remove(id);
    }
}
