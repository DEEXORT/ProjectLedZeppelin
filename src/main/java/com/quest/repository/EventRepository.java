package com.quest.repository;

import com.quest.entity.Achievement;
import com.quest.entity.Event;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class EventRepository {
    private Map<Long, Event> repository = new ConcurrentHashMap<>();
    private static AtomicLong id = new AtomicLong();

    public Collection<Event> getAll() {
        return repository.values();
    }

    public Event get(long id) {
        return repository.get(id);
    }

    public void create(Event object) {
        object.setId(id.incrementAndGet());
        update(object);
    }

    public void update(Event object) {
        repository.put(object.getId(), object);
    }

    public void delete(long id) {
        repository.remove(id);
    }
}
