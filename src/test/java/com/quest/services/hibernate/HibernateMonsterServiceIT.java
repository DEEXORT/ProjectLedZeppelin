package com.quest.services.hibernate;

import com.quest.entity.character.Monster;
import com.quest.entity.factory.MonsterFactory;
import com.quest.repository.RepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HibernateMonsterServiceIT extends ContainerIT {
    private HibernateMonsterService service;

    @BeforeEach
    void setUp() {
        service = new HibernateMonsterService(new RepositoryImpl<>(sessionCreator, Monster.class));
    }

    @AfterEach
    void tearDown() {
        service.getAll().forEach(service::delete);
    }

    @Test
    void shouldCreateMonster() {
        // given
        Monster monster = MonsterFactory.createGoblinMonster();

        // when
        service.create(monster);

        // then
        assertNotNull(monster.getId());
    }

    @Test
    void shouldGetMonster() {
        // given
        Monster monster = MonsterFactory.createGoblinMonster();
        service.create(monster);

        // when
        Optional<Monster> optional = service.get(monster.getId());

        // then
        assertTrue(optional.isPresent());
    }

    @Test
    void shouldUpdateMonster() {
        // given
        Monster monster = MonsterFactory.createGoblinMonster();
        service.create(monster);

        // when
        monster.setHealth(1);
        service.update(monster);

        // then
        Optional<Monster> optional = service.get(monster.getId());
        assertTrue(optional.isPresent());
        assertEquals(monster.getHealth(), optional.get().getHealth());
    }

    @Test
    void shouldDeleteMonster() {
        // given
        Monster monster = MonsterFactory.createGoblinMonster();
        service.create(monster);

        // when
        service.delete(monster);

        // then
        Optional<Monster> optional = service.get(monster.getId());
        assertFalse(optional.isPresent());
    }
}