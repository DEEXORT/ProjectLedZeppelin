package com.quest.services.hibernate;

import com.quest.entity.Achievement;
import com.quest.entity.Action;
import com.quest.entity.Event;
import com.quest.entity.QuestScene;
import com.quest.entity.character.Monster;
import com.quest.repository.RepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HibernateQuestServiceIT extends ContainerIT {
    private HibernateQuestService hibernateQuestService;
    private HibernateAchievementService hibernateAchievementService;
    private HibernateEventService hibernateEventService;
    private HibernateMonsterService hibernateMonsterService;

    @BeforeEach
    void setUp() {
        hibernateQuestService = new HibernateQuestService(new RepositoryImpl<>(sessionCreator, QuestScene.class),
                new RepositoryImpl<>(sessionCreator, Action.class));
        hibernateAchievementService = new HibernateAchievementService(new RepositoryImpl<>(sessionCreator, Achievement.class));
        hibernateEventService = new HibernateEventService(new RepositoryImpl<>(sessionCreator, Event.class));
        hibernateMonsterService = new HibernateMonsterService(new RepositoryImpl<>(sessionCreator, Monster.class));


    }

    @AfterEach
    void tearDown() {
        hibernateQuestService.getAll().forEach(hibernateQuestService::delete);
        hibernateAchievementService.getAll().forEach(hibernateAchievementService::delete);
        hibernateEventService.getAll().forEach(hibernateEventService::delete);
        hibernateMonsterService.getAll().forEach(hibernateMonsterService::delete);
    }

    @Test
    void shouldSaveAllScenes() {
        // then
        Collection<QuestScene> scenes = hibernateQuestService.getAll();
        assertEquals(26, scenes.size());
    }

    @Test
    void shouldSaveAllAchievements() {
        // then
        Collection<Achievement> achievements = hibernateAchievementService.getAll();
        assertNotEquals(0, achievements.size());
    }

    @Test
    void shouldSaveAllEvents() {
        // then
        Collection<Event> events = hibernateEventService.getAll();
        assertNotEquals(0, events.size());
    }
}