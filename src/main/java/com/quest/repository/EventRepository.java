package com.quest.repository;

import com.quest.entity.Achievement;
import com.quest.entity.Event;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class EventRepository implements Repository<Event> {
    private Map<Long, Event> repository = new ConcurrentHashMap<>();
    private static AtomicLong id = new AtomicLong();

    @Override
    public Collection<Event> getAll() {
        return repository.values();
    }

    @Override
    public Event get(long id) {
        return repository.get(id);
    }

    @Override
    public void create(Event object) {
        object.setId(id.incrementAndGet());
        update(object);
    }

    @Override
    public void update(Event object) {
        repository.put(object.getId(), object);
    }

    @Override
    public void delete(long id) {
        repository.remove(id);
    }
}
