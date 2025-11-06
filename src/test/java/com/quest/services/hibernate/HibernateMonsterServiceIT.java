package com.quest.services.hibernate;

import com.quest.dto.MonsterTo;
import com.quest.entity.factory.MonsterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HibernateMonsterServiceIT extends ContainerIT {
    private HibernateMonsterService service;

    @BeforeEach
    void setUp() {
        service = new HibernateMonsterService();
    }

    @Test
    void shouldCreateMonster() {
        // given
        MonsterTo monster = MonsterFactory.createGoblinMonster();

        // when
        service.create(monster);

        // then
        assertNotNull(monster.getId());
    }

    @Test
    void shouldGetMonster() {
        // given
        MonsterTo monster = MonsterFactory.createGoblinMonster();
        service.create(monster);

        // when
        MonsterTo monsterFromDb = service.get(monster.getId());

        // then
        assertNotNull(monsterFromDb);
    }

    @Test
    void shouldUpdateMonster() {
        // given
        MonsterTo monster = MonsterFactory.createGoblinMonster();
        service.create(monster);

        // when
        monster.setHealth(1);
        service.update(monster);

        // then
        MonsterTo monsterFromDb = service.get(monster.getId());
        assertNotNull(monsterFromDb);
        assertEquals(monster.getHealth(), monsterFromDb.getHealth());
    }

    @Test
    void shouldDeleteMonster() {
        // given
        MonsterTo monster = MonsterFactory.createGoblinMonster();
        service.create(monster);

        // when
        service.delete(monster);

        // then
        MonsterTo monsterFromDb = service.get(monster.getId());
        assertNull(monsterFromDb);
    }
}