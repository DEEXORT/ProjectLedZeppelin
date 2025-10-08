package com.quest.services.hibernate;

import com.quest.config.AbilityConfigLoader;
import com.quest.entity.Ability;
import com.quest.entity.User;
import com.quest.entity.character.Player;
import com.quest.repository.RepositoryImpl;
import com.quest.util.ResourceBundleManager;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HibernatePlayerServiceIT extends ContainerIT {
    private HibernatePlayerService hibernatePlayerService;
    private HibernateAbilityService hibernateAbilityService;
    private HibernateUserService hibernateUserService;
    private User user;

    @BeforeEach
    void setUp() {
        hibernatePlayerService = new HibernatePlayerService(new RepositoryImpl<>(sessionCreator, Player.class));
        hibernateAbilityService = new HibernateAbilityService(new RepositoryImpl<>(sessionCreator, Ability.class));
        hibernateUserService = new HibernateUserService(new RepositoryImpl<>(sessionCreator, User.class));
        AbilityConfigLoader abilityConfigLoader = new AbilityConfigLoader(hibernateAbilityService);
        abilityConfigLoader.loadAbilities();
        user = User.builder()
                .login("admin")
                .password("admin")
                .build();
        hibernateUserService.create(user);
    }

    Player buildPlayer(String name) {
        Player player = Player.builder()
                .name(name)
                .level(1)
                .userId(user.getId())
                .health(100)
                .maxHealth(300)
                .attack(199)
//                .questSceneId(3L)
                .experienceLevel(300)
                .experiencePoints(50)
                .build();
        Ability baseAttack = hibernateAbilityService.getByName(ResourceBundleManager.getSetting("ability.base_attack_name"));
        player.getAbilities().add(baseAttack);
        return player;
    }

    @AfterEach
    void tearDown() {
        hibernatePlayerService.getAll().forEach(hibernatePlayerService::delete);
        hibernateUserService.getAll().forEach(hibernateUserService::delete);
    }

    @Test
    void shouldCreatePlayer() {
        // given
        Player player = buildPlayer("player");

        // when
        hibernatePlayerService.create(player);

        // then
        assertNotNull(player.getId());
        assertEquals(1, hibernatePlayerService.getAll().size());
        assertEquals("player", player.getName());
        assertEquals(1, player.getLevel());
        assertEquals(100, player.getHealth());
        assertEquals(300, player.getMaxHealth());
        assertEquals(199, player.getAttack());
        assertEquals(300, player.getExperienceLevel());
        assertEquals(50, player.getExperiencePoints());
        assertEquals(1, player.getAbilities().size());
    }

    @Test
    void getAll() {
        // given
        Player player1 = buildPlayer("player1");
        Player player2 = buildPlayer("player2");
        hibernatePlayerService.create(player1);
        hibernatePlayerService.create(player2);

        // when
        Collection<Player> players = hibernatePlayerService.getAll();

        // then
        assertEquals(2, players.size());
    }

    @Test
    void get() {
        // given
        Player player = buildPlayer("player");
        hibernatePlayerService.create(player);

        // when
        Optional<Player> optional = hibernatePlayerService.get(player.getId());

        // then
        assertTrue(optional.isPresent());
    }

    @Test
    void update() {
        // given
        Player player = buildPlayer("player");
        hibernatePlayerService.create(player);
        player.setHealth(777);

        // when
        hibernatePlayerService.update(player);

        // then
        Optional<Player> optional = hibernatePlayerService.get(player.getId());
        assertEquals(player.getHealth(), optional.get().getHealth());
    }

    @Test
    void delete() {
        // given
        Player player = buildPlayer("player");
        hibernatePlayerService.create(player);

        // when
        hibernatePlayerService.delete(player);

        // then
        assertTrue(hibernatePlayerService.get(player.getId()).isEmpty());
    }

}