package com.quest.repository;

import com.quest.entity.QuestScene;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class QuestRepository implements Repository<QuestScene> {
    private final Map<Long, QuestScene> questSceneMap = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong();

    @Override
    public Collection<QuestScene> getAll() {
        return questSceneMap.values();
    }

    @Override
    public QuestScene get(long questId) {
        return questSceneMap.get(questId);
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
        questSceneMap.put(quest.getId(), quest);
    }

    @Override
    public void delete(long questId) {
        questSceneMap.remove(questId);
    }

}
