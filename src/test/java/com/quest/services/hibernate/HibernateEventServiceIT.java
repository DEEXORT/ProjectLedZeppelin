package com.quest.services.hibernate;

import com.quest.dto.EventTo;
import com.quest.dto.MonsterTo;
import com.quest.entity.factory.MonsterFactory;
import com.quest.util.EventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class HibernateEventServiceIT extends ContainerIT {
    private HibernateEventService eventService;
    private HibernateMonsterService monsterService;

    @BeforeEach
    void setUp() {
        eventService = new HibernateEventService();
        monsterService = new HibernateMonsterService();
    }


    @Test
    void shouldCreateEventWithMonster() {
        // given
        EventTo event = getEvent();

        // when
        eventService.create(event);

        // then
        Assertions.assertNotNull(event.getId());
    }

    private EventTo getEvent() {
        MonsterTo orc = MonsterFactory.createOrcMonster();
        Optional<MonsterTo> optionalMonster = monsterService.get(orc);
        if (optionalMonster.isPresent()) {
            orc.setId(optionalMonster.get().getId());
        } else {
            monsterService.create(orc);
        }
        return EventTo.builder()
                .monsterId(orc.getId())
                .type(EventType.BATTLE)
                .description("Battle description")
                .build();
    }

    @Test
    void shouldUpdateEventWithMonster() {
        // given
        EventTo event = getEvent();
        eventService.create(event);

        // when
        event.setDescription("Battle change description");
        eventService.update(event);

        // then
        Assertions.assertNotNull(event.getId());
        Assertions.assertEquals("Battle change description", event.getDescription());
    }

    @Test
    void shouldGetEventById() {
        // given
        EventTo event = getEvent();
        eventService.create(event);

        // when
        EventTo eventFromDb = eventService.get(event.getId());

        // then
        Assertions.assertNotNull(eventFromDb);
    }

    @Test
    void shouldDeleteEvent() {
        // given
        EventTo event = getEvent();
        eventService.create(event);

        // when
        eventService.delete(event);

        // then
        EventTo eventFromDb = eventService.get(event.getId());
        Assertions.assertNull(eventFromDb);
    }
}