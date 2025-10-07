package com.quest.services.hibernate;

import com.quest.entity.Ability;
import com.quest.repository.RepositoryImpl;
import org.junit.jupiter.api.*;

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
        Ability ability = Ability.builder()
                .value(100)
                .type(Ability.AbilityType.DAMAGE)
                .name("Test GetAll Ability")
                .description("Test GetAll Ability")
                .cooldown(3)
                .level(1)
                .levelRequirement(1)
                .build();
        service.create(ability);

        // when
        Collection<Ability> abilities = service.getAll();

        // then
        assertNotNull(abilities);
        assertFalse(abilities.isEmpty());
    }

    @Test
    void create() {
        //given
        Ability ability = Ability.builder()
                .value(100)
                .type(Ability.AbilityType.DAMAGE)
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
        Ability ability = Ability.builder()
                .value(100)
                .type(Ability.AbilityType.DAMAGE)
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
        Ability ability = Ability.builder()
                .value(100)
                .type(Ability.AbilityType.DAMAGE)
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