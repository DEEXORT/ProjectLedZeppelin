package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.config.SessionCreator;
import com.quest.entity.Ability;
import com.quest.repository.RepositoryImpl;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class HibernateAbilityServiceIT extends ContainerIT {
    private HibernateAbilityService service;


    @BeforeEach
    void setUp() {
        RepositoryImpl<Ability> repository = new RepositoryImpl<>(sessionCreator, Ability.class);
        service = new HibernateAbilityService(repository);
    }

    @Test
    void getAbility() {
    }

    @Test
    void getAll() {
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
                .levelRequirement(1)
                .build();

        //when
        service.create(ability);

        //then
        assertNotNull(service.get(ability.getId()));
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getByName() {
    }
}