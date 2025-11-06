package com.quest.services.hibernate;

import com.quest.dto.EventTo;
import com.quest.dto.QuestSceneTo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
class HibernateQuestServiceIT extends ContainerIT {
    private HibernateQuestService hibernateQuestService;
    private HibernateEventService hibernateEventService;
    private HibernateMonsterService hibernateMonsterService;

    @BeforeEach
    void setUp() {
        hibernateQuestService = new HibernateQuestService();
        hibernateEventService = new HibernateEventService();
        hibernateMonsterService = new HibernateMonsterService();
    }

    @Test
    void shouldSaveAllScenes() {
        // then
        Collection<QuestSceneTo> scenes = hibernateQuestService.getAll();
        assertNotEquals(0, scenes.size());
    }

    @Test
    void shouldSaveAllEvents() {
        // then
        Collection<EventTo> events = hibernateEventService.getAll();
        assertNotEquals(0, events.size());
    }
}