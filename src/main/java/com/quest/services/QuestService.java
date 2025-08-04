package com.quest.services;

import com.quest.entity.Action;
import com.quest.entity.QuestScene;
import com.quest.repository.ActionRepository;
import com.quest.repository.QuestRepository;

import java.util.Optional;

public class QuestService {
    private final QuestRepository questRepository;
    private final ActionRepository actionRepository;

    public QuestService(QuestRepository questRepository, ActionRepository actionRepository) {
        this.questRepository = questRepository;
        this.actionRepository = actionRepository;
    }

    public Optional<QuestScene> create(QuestScene questScene) {
        questRepository.create(questScene);
        for (Action action : questScene.getActions()) {
            action.setQuestSceneId(questScene.getId());
            actionRepository.create(action);
        }
        return Optional.of(questScene);
    }

    public Optional<QuestScene> update(QuestScene questScene) {
        questRepository.update(questScene);
        return Optional.of(questScene);
    }

    public Optional<QuestScene> get(long id) {
        return Optional.ofNullable(questRepository.get(id));
    }
}
