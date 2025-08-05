package com.quest.repository;

import com.quest.entity.QuestScene;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class QuestRepository implements Repository<QuestScene> {
    private final Map<Long, QuestScene> repository = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong();

    @Override
    public Collection<QuestScene> getAll() {
        return repository.values();
    }

    @Override
    public QuestScene get(long questId) {
        return repository.get(questId);
    }

    @Override
    public void create(QuestScene quest) {
        if (quest.getId() == null) {
            quest.setId(id.incrementAndGet());
        }
        update(quest);
    }

    @Override
    public void update(QuestScene quest) {
        repository.put(quest.getId(), quest);
    }

    @Override
    public void delete(long questId) {
        repository.remove(questId);
    }

}
