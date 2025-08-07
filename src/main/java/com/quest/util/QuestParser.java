package com.quest.util;

import com.quest.entity.Achievement;
import com.quest.entity.Action;
import com.quest.entity.QuestScene;
import com.quest.exception.AchievementNotCreateException;
import com.quest.services.AchievementService;
import com.quest.services.QuestService;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Data
public class QuestParser {
    private final QuestService questService;
    private final AchievementService achievementService;
    private Logger logger = LogManager.getLogger(QuestParser.class);

    public QuestParser(QuestService questService, AchievementService achievementService) {
        this.questService = questService;
        this.achievementService = achievementService;
    }

    /*
    Шаблон для парсинга сценария:
    ## id
    $ text <ach> achievement </ach>
    * action -> next_id

    где id - уникальный идентификатор сцены
    text - повествование сцены
    action - действие для пользователя
    achievement - достижение, с которой заканчивается игра
    next_id - идентификатор сцены, к которой приведет действие
     */

    public void parseText(String questText) {
        // Разделяем текст сценария квеста на отдельные сцены
        String[] questScenes = questText.split(ParseConst.SCENE_DELIMITER);
        for (int i = 1; i < questScenes.length; i++) {

            // Делим на составляющие: (questId + questScene) / (action + nextQuestId)
            String[] questSceneContent = questScenes[i].split(ParseConst.ACTION_DELIMITER);
            // Извлекаем текст сцены и создаем объект: questId / questScene / (action + nextQuestId)
            String[] idAndDescription = questSceneContent[0].split(ParseConst.SCENE_TEXT_DELIMITER);
            Long questSceneId = Long.parseLong(idAndDescription[0].trim());

            logger.info("Создание сцены id = {}", questSceneId);

            String questSceneDescription = idAndDescription[1].replace("\n", "<br>");
            QuestScene scene = QuestScene.builder()
                    .id(questSceneId)
                    .descriptionScene(questSceneDescription)
                    .actions(new ArrayList<>())
                    .build();

            // Если есть достижение в сцене, то сохранить в БД
            if (questSceneDescription.contains(ParseConst.ACHIEVEMENT_DELIMITER_START) ||
                    questSceneDescription.contains(ParseConst.ACHIEVEMENT_DELIMITER_END)) {
                logger.info("Создание достижения...");
                Achievement achievement = saveAchievement(questSceneDescription, questSceneId);
                String updatedQuestSceneDescription =
                        questSceneDescription
                                .replace(ParseConst.ACHIEVEMENT_DELIMITER_START, "")
                                .replace(ParseConst.ACHIEVEMENT_DELIMITER_END, "");
                scene.setDescriptionScene(updatedQuestSceneDescription);
                scene.setAchievement(achievement);
            }

            // Извлекаем действия и создаем для них объекты
            for (int j = 1; j < questSceneContent.length; j++) {
                String[] actionLine = questSceneContent[j].split(ParseConst.TARGET_SCENE_DELIMITER);
                String actionText = actionLine[0];
                Long nextQuestSceneId = Long.parseLong(actionLine[1].trim());
                Action action = Action.builder()
                        .actionText(actionText)
                        .questSceneId(questSceneId)
                        .nextQuestSceneId(nextQuestSceneId)
                        .build();
                scene.getActions().add(action);
            }

            // Сохраняем сцену и действия в БД
            Optional<QuestScene> questScene = questService.create(scene);
            questScene.ifPresent(value -> logger.info("QuestScene created: {}", value.getId()));
        }
    }

    private Achievement saveAchievement(String questSceneDescription, long questSceneId) {
        Pattern pattern = Pattern.compile("<ach>(.*?)</ach>");
        Matcher matcher = pattern.matcher(questSceneDescription);

        if (matcher.find()) {
            String achievementText = matcher.group(1);
            Achievement achievement = Achievement.builder()
                    .text(achievementText)
                    .questSceneId(questSceneId)
                    .build();
            achievementService.create(achievement);
            logger.info("Achievement created: {}", achievement);
            return achievement;
        } else {
            String errorMessage = "Achievement not create";
            logger.error(errorMessage);
            throw new AchievementNotCreateException(errorMessage);
        }
    }
}