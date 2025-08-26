package com.quest.services;

import com.quest.entity.Action;
import com.quest.entity.QuestScene;
import com.quest.repository.ActionRepository;
import com.quest.repository.QuestRepository;
import com.quest.util.KeyAttribute;
import com.quest.util.ResourceBundleManager;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class QuestService {
    private final QuestRepository questRepository;
    private final ActionRepository actionRepository;

    // TODO: replace to const from properties
    private final int percentageChanceBattle = ThreadLocalRandom.current().nextInt(100);

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

    public boolean isBattleEvent(HttpServletRequest request) {
        return !((boolean) request.getSession().getAttribute(KeyAttribute.BATTLE_FLAG))
                && (percentageChanceBattle < getMaxBattleChance()); // TODO: replace hard int to int from properties
    }

    private int getMaxBattleChance() {
        return Integer.parseInt(ResourceBundleManager.getSetting("event.max_chance_battle"));
    }
}
