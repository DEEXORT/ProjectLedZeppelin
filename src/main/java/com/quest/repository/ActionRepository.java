package com.quest.repository;

import com.quest.entity.Action;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ActionRepository implements Repository<Action> {
    private static AtomicLong id = new AtomicLong();
    private Map<Long, Action> repository = new ConcurrentHashMap<>();

    @Override
    public Collection<Action> getAll() {
        return repository.values();
    }

    @Override
    public Action get(long id) {
        return repository.get(id);
    }

    @Override
    public void create(Action action) {
        action.setId(id.incrementAndGet());
        update(action);
    }

    @Override
    public void update(Action action) {
        repository.put(action.getId(), action);
    }

    @Override
    public void delete(long id) {
        repository.remove(id);
    }
}
