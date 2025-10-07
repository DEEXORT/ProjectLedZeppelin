package com.quest.repository;

import com.quest.entity.QuestScene;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class QuestRepository {
    private final Map<Long, QuestScene> repository = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong();

    public Collection<QuestScene> getAll() {
        return repository.values();
    }

    public QuestScene get(long questId) {
        return repository.get(questId);
    }

    public void create(QuestScene quest) {
        if (quest.getId() == null) {
            quest.setId(id.incrementAndGet());
        }
        update(quest);
    }

    public void update(QuestScene quest) {
        repository.put(quest.getId(), quest);
    }

    public void delete(long questId) {
        repository.remove(questId);
    }

}
