package com.quest.services.hibernate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.config.AbilityConfigLoader;
import com.quest.dto.AbilityTo;
import com.quest.dto.PlayerTo;
import com.quest.dto.UserTo;
import com.quest.util.ResourceBundleManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class HibernatePlayerServiceIT extends ContainerIT {
    private static HibernatePlayerService hibernatePlayerService;
    private static HibernateAbilityService hibernateAbilityService;
    private static HibernateUserService hibernateUserService;
    private static UserTo user;

    @BeforeAll
    static void setUp() {
        hibernatePlayerService = new HibernatePlayerService();
        hibernateAbilityService = new HibernateAbilityService();
        hibernateUserService = new HibernateUserService();
        AbilityConfigLoader abilityConfigLoader = new AbilityConfigLoader(hibernateAbilityService);
        abilityConfigLoader.loadAbilities();
        user = UserTo.builder()
                .login("adminTest")
                .password("adminTest")
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
                .experienceLevel(300)
                .experiencePoints(50)
                .build();
        AbilityTo baseAttack = hibernateAbilityService.getByName(ResourceBundleManager.getSetting("ability.base_attack_name"));
        player.getAbilities().add(baseAttack);
        return player;
    }

    @Test
    void shouldCreatePlayer() {
        // given
        PlayerTo player = buildPlayer("player");

        // when
        hibernatePlayerService.create(player);

        // then
        assertNotNull(player.getId());
        assertNotEquals(0, hibernatePlayerService.getAll().size());

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
        int countPlayersBefore = hibernatePlayerService.getAll().size();
        PlayerTo player1 = buildPlayer("player1");
        PlayerTo player2 = buildPlayer("player2");
        hibernatePlayerService.create(player1);
        hibernatePlayerService.create(player2);

        // when
        Collection<PlayerTo> players = hibernatePlayerService.getAll();

        // then
        assertNotEquals(0, players.size());
    }

    @Test
    void get() {
        // given
        PlayerTo player = buildPlayer("player");
        hibernatePlayerService.create(player);

        // when
        PlayerTo optional = hibernatePlayerService.get(player.getId());

        // then
        assertNotNull(optional);
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
        PlayerTo optional = hibernatePlayerService.get(player.getId());
        assertEquals(player.getHealth(), optional.getHealth());
    }

}