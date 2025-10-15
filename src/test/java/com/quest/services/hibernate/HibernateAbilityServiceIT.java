package com.quest.services.hibernate;

import com.quest.dto.AbilityTo;
import com.quest.entity.Ability;
import com.quest.entity.AbilityType;
import com.quest.repository.RepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class HibernateAbilityServiceIT extends ContainerIT {
    private HibernateAbilityService service;


    @BeforeEach
    void setUp() {
        RepositoryImpl<Ability> repository = new RepositoryImpl<>(sessionCreator, Ability.class);
        service = new HibernateAbilityService(repository);
    }

    @Test
    void getAll() {
        //given
        AbilityTo ability = AbilityTo.builder()
                .value(100)
                .type(AbilityType.DAMAGE)
                .name("Test GetAll Ability")
                .description("Test GetAll Ability")
                .cooldown(3)
                .level(1)
                .levelRequirement(1)
                .build();
        service.create(ability);

        // when
        Collection<AbilityTo> abilities = service.getAll();

        // then
        assertNotNull(abilities);
        assertFalse(abilities.isEmpty());
    }

    @Test
    void create() {
        //given
        AbilityTo ability = AbilityTo.builder()
                .value(100)
                .type(AbilityType.DAMAGE)
                .name("Damage Ability")
                .description("Damage Ability")
                .cooldown(3)
                .level(1)
                .levelRequirement(1)
                .build();

        //when
        service.create(ability);

        //then
        assertNotNull(service.get(ability.getId()));
    }

    @Test
    void update() {
        //given
        AbilityTo ability = AbilityTo.builder()
                .value(100)
                .type(AbilityType.DAMAGE)
                .name("Test Update Ability")
                .description("Test Update Ability")
                .cooldown(3)
                .level(1)
                .levelRequirement(1)
                .build();
        service.create(ability);

        // when
        ability.setLevel(2);
        service.update(ability);

        // then
        assertEquals(2, service.getByName("Test Update Ability").getLevel());
    }

    @Test
    void delete() {
        //given
        AbilityTo ability = AbilityTo.builder()
                .value(100)
                .type(AbilityType.DAMAGE)
                .name("Test Delete Ability")
                .description("Test Delete Ability")
                .cooldown(3)
                .level(1)
                .levelRequirement(1)
                .build();
        service.create(ability);

        // when
        service.delete(ability);

        // then
        assertTrue(service.get(ability.getId()).isEmpty());
    }
}