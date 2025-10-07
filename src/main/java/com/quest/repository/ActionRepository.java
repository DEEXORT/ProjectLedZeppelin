package com.quest.repository;

import com.quest.entity.Action;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ActionRepository {
    private static AtomicLong id = new AtomicLong();
    private Map<Long, Action> repository = new ConcurrentHashMap<>();

    public Collection<Action> getAll() {
        return repository.values();
    }

    public Action get(long id) {
        return repository.get(id);
    }

    public void create(Action action) {
        action.setId(id.incrementAndGet());
        update(action);
    }

    public void update(Action action) {
        repository.put(action.getId(), action);
    }

    public void delete(long id) {
        repository.remove(id);
    }
}
