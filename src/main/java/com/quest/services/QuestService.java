package com.quest.services;

import com.quest.entity.Action;
import com.quest.entity.Monster;
import com.quest.entity.QuestScene;
import com.quest.repository.ActionRepository;
import com.quest.repository.QuestRepository;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.ThreadContext;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class QuestService {
    private final QuestRepository questRepository;
    private final ActionRepository actionRepository;
    private final MonsterService monsterService;
    private final int percentageChanceBattle = ThreadLocalRandom.current().nextInt(100);

    public QuestService(QuestRepository questRepository, ActionRepository actionRepository, MonsterService monsterService) {
        this.questRepository = questRepository;
        this.actionRepository = actionRepository;
        this.monsterService = monsterService;
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
                && (percentageChanceBattle < 100); // TODO: replace hard int to int from properties
    }
}
