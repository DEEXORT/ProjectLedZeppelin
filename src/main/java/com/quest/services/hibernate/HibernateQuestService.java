package com.quest.services.hibernate;

import com.quest.config.ConfigApplication;
import com.quest.config.ServiceLocator;
import com.quest.entity.Action;
import com.quest.entity.QuestScene;
import com.quest.repository.Repository;
import com.quest.repository.RepositoryImpl;
import com.quest.services.resolver.QuestParser;
import com.quest.util.KeyAttribute;
import com.quest.util.ResourceBundleManager;
import com.quest.util.ResourcePath;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class HibernateQuestService extends AbstractBaseService<QuestScene> {
    private static final Logger logger = LogManager.getLogger(ConfigApplication.class);
    private final RepositoryImpl<Action> actionRepository;
    // TODO: replace to const from properties
    private final int percentageChanceBattle =
            ThreadLocalRandom.current().nextInt(
                    Integer.parseInt(ResourceBundleManager.getSetting("event.max_chance_battle"))
            );

    public HibernateQuestService() {
        super(ServiceLocator.getService(RepositoryImpl.class, QuestScene.class));
        actionRepository = ServiceLocator.getService(RepositoryImpl.class, Action.class);
    }

    public HibernateQuestService(Repository<QuestScene> repository, RepositoryImpl<Action> actionRepository) {
        super(repository);
        this.actionRepository = actionRepository;
    }

    @Transactional
    public void saveAllScenes(Map<Long, QuestScene> scenes) {
        Map<Long, Long> idMapping = new HashMap<>();
        for (QuestScene scene : scenes.values()) {
            repository.create(scene);
            idMapping.put(scene.getFileId(), scene.getId());
        }
        updateActions(scenes, idMapping);
    }

    private void updateActions(Map<Long, QuestScene> scenes, Map<Long, Long> idMapping) {
        for (QuestScene scene : scenes.values()) {
            for (Action action : scene.getActions()) {
                // Replace fileId to generated ID by database
                action.setNextQuestSceneId(idMapping.get(action.getNextQuestSceneId()));
                actionRepository.update(action);
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
            logger.error("Error reading quest file");
            throw new RuntimeException("Error reading quest file", e);
        }
        logger.info("Loaded quest repository");
    }

    public QuestScene getFirstScene() {
        QuestScene patternScene = QuestScene.builder()
                .type(QuestScene.Type.START)
                .build();
        Optional<QuestScene> startScene = repository.find(patternScene).findFirst();
        if (startScene.isPresent()) {
            return startScene.get();
        } else {
            logger.error("No scene found");
            throw new RuntimeException("No scene found");
        }
    }

    public QuestScene getBattleDeathScene() {
        QuestScene patternScene = QuestScene.builder()
                .type(QuestScene.Type.BATTLE_DEATH)
                .build();
        Optional<QuestScene> deathScene = repository.find(patternScene).findFirst();
        if (deathScene.isPresent()) {
            return deathScene.get();
        } else {
            logger.error("No scene with battle death found");
            throw new RuntimeException("No scene with battle death found");
        }
    }
}
