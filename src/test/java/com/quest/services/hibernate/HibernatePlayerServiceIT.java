package com.quest.services.hibernate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.config.AbilityConfigLoader;
import com.quest.dto.AbilityTo;
import com.quest.dto.PlayerTo;
import com.quest.dto.UserTo;
import com.quest.entity.Ability;
import com.quest.entity.User;
import com.quest.entity.character.Player;
import com.quest.repository.RepositoryImpl;
import com.quest.util.ResourceBundleManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HibernatePlayerServiceIT extends ContainerIT {
    private HibernatePlayerService hibernatePlayerService;
    private HibernateAbilityService hibernateAbilityService;
    private HibernateUserService hibernateUserService;
    private UserTo user;

    @BeforeEach
    void setUp() {
        hibernatePlayerService = new HibernatePlayerService(new RepositoryImpl<>(sessionCreator, Player.class));
        hibernateAbilityService = new HibernateAbilityService(new RepositoryImpl<>(sessionCreator, Ability.class));
        hibernateUserService = new HibernateUserService(new RepositoryImpl<>(sessionCreator, User.class));
        AbilityConfigLoader abilityConfigLoader = new AbilityConfigLoader(hibernateAbilityService);
        abilityConfigLoader.loadAbilities();
        user = UserTo.builder()
                .login("admin")
                .password("admin")
                .build();
        hibernateUserService.create(user);
    }

    PlayerTo buildPlayer(String name) {
        PlayerTo player = PlayerTo.builder()
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
        AbilityTo baseAttack = hibernateAbilityService.getByName(ResourceBundleManager.getSetting("ability.base_attack_name"));
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
        PlayerTo player = buildPlayer("player");

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
        PlayerTo player1 = buildPlayer("player1");
        PlayerTo player2 = buildPlayer("player2");
        hibernatePlayerService.create(player1);
        hibernatePlayerService.create(player2);

        // when
        Collection<PlayerTo> players = hibernatePlayerService.getAll();

        // then
        assertEquals(2, players.size());
    }

    @Test
    void get() {
        // given
        PlayerTo player = buildPlayer("player");
        hibernatePlayerService.create(player);

        // when
        Optional<PlayerTo> optional = hibernatePlayerService.get(player.getId());

        // then
        assertTrue(optional.isPresent());
    }

    @Test
    void update() {
        // given
        PlayerTo player = buildPlayer("player");
        hibernatePlayerService.create(player);
        player.setHealth(777);

        // when
        hibernatePlayerService.update(player);

        // then
        Optional<PlayerTo> optional = hibernatePlayerService.get(player.getId());
        assertEquals(player.getHealth(), optional.get().getHealth());
    }

    @Test
    void delete() {
        // given
        PlayerTo player = buildPlayer("player");
        hibernatePlayerService.create(player);

        // when
        hibernatePlayerService.delete(player);

        // then
        assertTrue(hibernatePlayerService.get(player.getId()).isEmpty());
    }

}