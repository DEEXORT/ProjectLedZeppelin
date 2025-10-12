package com.quest.services.hibernate;

import com.quest.entity.Event;
import com.quest.entity.character.Monster;
import com.quest.entity.factory.MonsterFactory;
import com.quest.repository.RepositoryImpl;
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
        eventService = new HibernateEventService(new RepositoryImpl<>(sessionCreator, Event.class));
        monsterService = new HibernateMonsterService(new RepositoryImpl<>(sessionCreator, Monster.class));
    }

//    @AfterEach
//    void tearDown() {
//        eventService.getAll().forEach(eventService::delete);
//        monsterService.getAll().forEach(monsterService::delete);
//    }

    @Test
    void shouldCreateEventWithMonster() {
        // given
        Event event = getEvent();

        // when
        eventService.create(event);

        // then
        Assertions.assertNotNull(event.getId());
    }

    private Event getEvent() {
        Monster orc = MonsterFactory.createOrcMonster();
        Optional<Monster> optionalMonster = monsterService.get(orc);
        if (optionalMonster.isPresent()) {
            orc.setId(optionalMonster.get().getId());
        } else {
            monsterService.create(orc);
        }
        return Event.builder()
                .monsterId(orc.getId())
                .type(EventType.BATTLE)
                .description("Battle description")
                .build();
    }

    @Test
    void shouldUpdateEventWithMonster() {
        // given
        Event event = getEvent();
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
        Event event = getEvent();
        eventService.create(event);

        // when
        Optional<Event> optional = eventService.get(event.getId());

        // then
        Assertions.assertTrue(optional.isPresent());
    }

    @Test
    void shouldDeleteEvent() {
        // given
        Event event = getEvent();
        eventService.create(event);

        // when
        eventService.delete(event);

        // then
        Optional<Event> optional = eventService.get(event.getId());
        Assertions.assertFalse(optional.isPresent());
    }
}