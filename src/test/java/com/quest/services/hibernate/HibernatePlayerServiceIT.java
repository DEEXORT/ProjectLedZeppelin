package com.quest.services.hibernate;

import com.quest.config.AbilityConfigLoader;
import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.entity.character.Player;
import com.quest.repository.RepositoryImpl;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class HibernatePlayerServiceIT extends ContainerIT {
    private HibernatePlayerService hibernatePlayerService;
    private HibernateAbilityService hibernateAbilityService;

    @BeforeEach
    void setUp() {
        hibernatePlayerService = new HibernatePlayerService(new RepositoryImpl<>(sessionCreator, Player.class));
        hibernateAbilityService = new HibernateAbilityService(new RepositoryImpl<>(sessionCreator, Ability.class));
        AbilityConfigLoader abilityConfigLoader = new AbilityConfigLoader(hibernateAbilityService);
        abilityConfigLoader.loadAbilities();
    }

    @Test
    void shouldCreatePlayerWithoutAbilities() {
        // given
        Player test = Player.builder()
                .name("test")
                .level(1)
                .userId(1L)
                .health(100)
                .maxHealth(300)
                .attack(199)
                .questSceneId(3L)
                .experienceLevel(300)
                .experiencePoints(50)
                .build();
        // TODO: Присвоить способности самому, т.к. больше не инициализируется в билдере

        // when
        hibernatePlayerService.create(test);

        // then
        assertNotNull(test.getCharacterId());
        assertEquals(1, hibernatePlayerService.getAll().size());
        assertEquals("test", test.getName());
        assertEquals(1, test.getLevel());
        assertEquals(100, test.getHealth());
        assertEquals(300, test.getMaxHealth());
        assertEquals(199, test.getAttack());
        assertEquals(3, test.getQuestSceneId());
        assertEquals(300, test.getExperienceLevel());
        assertEquals(50, test.getExperiencePoints());
        assertEquals(1, test.getAbilities().size());
        System.out.println(test.getAbilities().get(0).getName());
    }

    @Test
    void getAll() {
    }

    @Test
    void get() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @AfterAll
    static void tearDown() {
    }
}