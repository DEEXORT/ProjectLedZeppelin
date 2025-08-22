package com.quest;

import com.quest.config.ConfigApplication;
import com.quest.config.ServiceLocator;
import com.quest.entity.character.Monster;
import com.quest.entity.character.Player;
import com.quest.entity.User;
import com.quest.entity.factory.MonsterFactory;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigIT {
    protected final HttpServletRequest request;
    protected final HttpServletResponse response;
    protected final HttpSession session;
    protected final RequestDispatcher requestDispatcher;
    protected static final ServletConfig servletConfig = mock(ServletConfig.class);
    protected static ConfigApplication config;
    protected Player playerTest;
    protected User userTest;
    protected Monster monsterTest;

    @BeforeAll
    static void initOnce() {
        config = ServiceLocator.getService(ConfigApplication.class);
        config.initApplication();
    }

    protected ConfigIT() {
        // mocks
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        requestDispatcher = mock(RequestDispatcher.class);

        userTest = User.builder()
                .login("user")
                .password("password")
                .id(1L)
                .build();
        playerTest = Player.builder()
                .id(1L)
                .userId(userTest.getId())
                .name("player")
                .level(1)
                .maxHealth(100)
                .health(100)
                .attack(10)
                .build();
        userTest = userTest.toBuilder()
                .playerId(playerTest.getId())
                .build();
        monsterTest = MonsterFactory.createMonster("monster", 1, 100, 10)
                .toBuilder()
                .id(1L)
                .build();

        // config mocks
        when(request.getSession()).thenReturn(session);
    }
}
