package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.dto.ActionTo;
import com.quest.dto.QuestSceneTo;
import com.quest.entity.Action;
import com.quest.entity.QuestScene;
import com.quest.entity.QuestSceneType;
import com.quest.mapping.Dto;
import com.quest.repository.RepositoryImpl;
import com.quest.services.resolver.QuestParser;
import com.quest.util.KeyAttribute;
import com.quest.util.ResourceBundleManager;
import com.quest.util.ResourcePath;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
public class HibernateQuestService implements BaseService<QuestSceneTo> {
    private final RepositoryImpl<QuestScene> repository;
    private final RepositoryImpl<Action> actionRepository;
    private Dto dto = Dto.MAPPER;

    private final int percentageChanceBattle =
            ThreadLocalRandom.current().nextInt(
                    Integer.parseInt(ResourceBundleManager.getSetting("event.max_chance_battle"))
            );

    public HibernateQuestService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, QuestScene.class);
        this.actionRepository = ServiceLocator.getService(RepositoryImpl.class, Action.class);
    }

    public QuestSceneTo get(long id) {
        return dto.from(repository.get(id));
    }

    public Collection<QuestSceneTo> getAll() {
        return repository.getAll().stream().map(dto::from).collect(Collectors.toList());
    }

    public void create(QuestSceneTo sceneTo) {
        QuestScene questScene = dto.from(sceneTo);
        repository.create(questScene);
        sceneTo.setId(questScene.getId());
    }

    public void update(QuestSceneTo sceneTo) {
        repository.update(dto.from(sceneTo));
    }

    public void saveAllScenes(Map<Long, QuestSceneTo> sceneToMap) {
        Map<Long, Long> idMapping = new HashMap<>();
        for (QuestSceneTo sceneTo : sceneToMap.values()) {
            // Create questScene in database
            QuestScene sceneEntity = dto.from(sceneTo);
            repository.create(sceneEntity);

            // Update DTO
            sceneTo = dto.from(sceneEntity);
            sceneToMap.put(sceneTo.getFileId(), sceneTo);

            // Recording mapping between id from file and id obtained from db
            idMapping.put(sceneTo.getFileId(), sceneTo.getId());
        }
        updateActions(sceneToMap, idMapping);
    }

    private void updateActions(Map<Long, QuestSceneTo> scenes, Map<Long, Long> idMapping) {
        for (QuestSceneTo scene : scenes.values()) {
            for (ActionTo action : scene.getActions()) {
                // Replace fileId to generated ID by database
                action.setNextQuestSceneId(idMapping.get(action.getNextQuestSceneId()));
                actionRepository.update(dto.from(action));
            }
        }
    }

    public boolean isBattleEvent(HttpServletRequest request) {
        return !((boolean) request.getSession().getAttribute(KeyAttribute.BATTLE_FLAG))
                && (percentageChanceBattle < getMaxBattleChance()); // TODO: replace hard int to int from properties
    }

    private int getMaxBattleChance() {
        return Integer.parseInt(ResourceBundleManager.getSetting("event.max_chance_battle"));
    }

    public void fillQuestRepository() {
        QuestParser questParser = ServiceLocator.getService(QuestParser.class);
        try {
            URL resource = Objects.requireNonNull(
                    getClass().getResource(ResourcePath.QUEST));
            Path pathQuestFile = Paths.get(resource.toURI());
            String dataQuest = Files.readString(pathQuestFile, StandardCharsets.UTF_8);
            questParser.parseText(dataQuest);
        } catch (IOException | URISyntaxException e) {
            log.error("Error reading quest file");
            throw new RuntimeException("Error reading quest file", e);
        }
        log.info("Loaded quest repository");
    }

    public QuestSceneTo getFirstScene() {
        QuestScene patternScene = QuestScene.builder()
                .type(QuestSceneType.START)
                .build();
        Optional<QuestSceneTo> startScene = repository.find(patternScene).map(dto::from).findFirst();
        if (startScene.isPresent()) {
            return startScene.get();
        } else {
            log.error("No scene found");
            throw new RuntimeException("No scene found");
        }
    }

    public QuestScene getBattleDeathScene() {
        QuestScene patternScene = QuestScene.builder()
                .type(QuestSceneType.BATTLE_DEATH)
                .build();
        Optional<QuestScene> deathScene = repository.find(patternScene).findFirst();
        if (deathScene.isPresent()) {
            return deathScene.get();
        } else {
            log.error("No scene with battle death found");
            throw new RuntimeException("No scene with battle death found");
        }
    }

    public void delete(QuestSceneTo sceneTo) {
        repository.delete(dto.from(sceneTo));
    }
}
