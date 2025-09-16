package com.quest.util;

import com.quest.entity.*;
import com.quest.entity.character.Monster;
import com.quest.entity.factory.MonsterFactory;
import com.quest.exception.AchievementNotCreateException;
import com.quest.exception.QuestNotFoundException;
import com.quest.services.AchievementService;
import com.quest.services.EventService;
import com.quest.services.MonsterService;
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
    private final EventService eventService;
    private final AchievementService achievementService;
    private final MonsterService monsterService;
    private Logger logger = LogManager.getLogger(QuestParser.class);
    private final Pattern PATTERN_EVENT = Pattern.compile(
            "<event\\s+" +
                    "(?:" +
                    "(?:type=\"(?<type>[^\"]*)\"|stat=\"(?<stat>[^\"]*)\"|value=\"(?<value>\\d+)\"|text=\"(?<text>[^\"]*)\")\\s*" +
                    ")+" + // "+" — требует минимум 1 атрибут
                    ">"
    );
    private final Pattern PATTERN_NEXT_QUEST_ID = Pattern.compile("(?<id>\\d+)");
    private final Pattern PATTERN_MONSTER_STAT = Pattern.compile(
            "<event\\s+" +
                    "type=\"battle\"\\s+" +
                    // Проверка на обязательные атрибуты
                    "(?=.*?name=\"[^\"]*\")" +
                    "(?=.*?level=\"[^\"]*\")" +
                    "(?=.*?health=\"[^\"]*\")" +
                    "(?=.*?attack=\"[^\"]*\")" +
                    "(?=.*?text=\"[^\"]*\")" +
                    // Захват атрибутов
                    "(?:" +
                    "(?:name=\"(?<name>[^\"]*)\")\\s+" +
                    "|(?:level=\"(?<level>\\d+)\")\\s+" +
                    "|(?:health=\"(?<health>\\d+)\")\\s+" +
                    "|(?:attack=\"(?<attack>\\d+)\")\\s+" +
                    "|(?:text=\"(?<text>[^\"]*)\")\\s*" +
                    "){5}>\\s*");

    public QuestParser(QuestService questService, AchievementService achievementService, EventService eventService, MonsterService monsterService) {
        this.questService = questService;
        this.eventService = eventService;
        this.achievementService = achievementService;
        this.monsterService = monsterService;
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
                long nextQuestSceneId = getQuestSceneId(actionLine[1]);
                long eventId = saveEvent(actionLine[1]);
                Action action = Action.builder()
                        .actionText(actionText)
                        .questSceneId(questSceneId)
                        .nextQuestSceneId(nextQuestSceneId)
                        .eventId(eventId == 0 ? null : eventId)
                        .build();
                logger.info("Action: {}", action);
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

    private long saveEvent(String encodedQuestSceneIdAndEvent) {
        Matcher matcher = PATTERN_EVENT.matcher(encodedQuestSceneIdAndEvent.trim());
        if (matcher.find()) {
            EventType eventType = EventType.valueOf(matcher.group(EventAttribute.TYPE).toUpperCase());
            // TODO: Add other fields for Event
            Event event = Event.builder()
                    .type(eventType)
                    .value(Integer.parseInt(matcher.group(EventAttribute.VALUE)))
                    .stat(matcher.group(EventAttribute.STAT) == null ? "" : matcher.group(EventAttribute.STAT))
                    .description(matcher.group(EventAttribute.TEXT))
                    .build();
            eventService.create(event);
            logger.info("Event created: {}", event);
            return event.getId();
        } else {
            return saveBattleEvent(encodedQuestSceneIdAndEvent.trim());
        }
    }

    private long saveBattleEvent(String questSceneDescription) {
        Matcher matcher = PATTERN_MONSTER_STAT.matcher(questSceneDescription);
        if (matcher.find()) {
            String nameMonster = matcher.group(EventAttribute.NAME);
            int level = Integer.parseInt(matcher.group(EventAttribute.LEVEL));
            int health = Integer.parseInt(matcher.group(EventAttribute.HEALTH));
            int attack = Integer.parseInt(matcher.group(EventAttribute.ATTACK));
            Monster monster = MonsterFactory.createMonster(nameMonster, level, health, attack, Monster.MonsterType.BOSS);
            monsterService.create(monster);
            logger.info("Monster created: {}", monster);

            String questDescription = matcher.group(EventAttribute.TEXT);
            Event event = Event.builder()
                    .type(EventType.BATTLE)
                    .monsterId(monster.getCharacterId())
                    .description(questDescription)
                    .build();
            eventService.create(event);
            logger.info("Event with battle created: {}", event);
            return event.getId();
        }
        return 0;
    }

    private long getQuestSceneId(String encodedQuestSceneIdAndEvent) {
        Matcher matcher = PATTERN_NEXT_QUEST_ID.matcher(encodedQuestSceneIdAndEvent);
        if (matcher.find()) {
            return Long.parseLong(matcher.group("id"));
        } else {
            logger.error("Not found quest scene id: {}", encodedQuestSceneIdAndEvent);
            throw new QuestNotFoundException("Not found quest scene id: " + encodedQuestSceneIdAndEvent);
        }
    }
}