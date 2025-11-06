package com.quest;

import com.quest.config.ConfigApplication;
import com.quest.config.ServiceLocator;
import com.quest.dto.MonsterTo;
import com.quest.dto.PlayerTo;
import com.quest.dto.UserTo;
import com.quest.entity.character.MonsterType;
import com.quest.entity.factory.MonsterFactory;
import com.quest.services.hibernate.ContainerIT;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigIT extends ContainerIT {
    protected final HttpServletRequest request;
    protected final HttpServletResponse response;
    protected final HttpSession session;
    protected final RequestDispatcher requestDispatcher;
    protected static final ServletConfig servletConfig = mock(ServletConfig.class);
    protected static ConfigApplication config;
    protected PlayerTo playerTest;
    protected UserTo userTest;
    protected MonsterTo monsterTest;

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

        userTest = UserTo.builder()
                .login("user")
                .password("password")
                .id(1L)
                .build();
        playerTest = PlayerTo.builder()
                .id(1L)
                .userId(userTest.getId())
                .name("player")
                .level(1)
                .maxHealth(100)
                .health(100)
                .attack(10)
                .build();
        playerTest.initBaseAbilities();

        userTest = userTest.toBuilder()
                .characterId(playerTest.getId())
                .build();
        monsterTest = MonsterFactory.createMonster("monster", 1, 100, 10, MonsterType.COMMON)
                .toBuilder()
                .id(1L)
                .build();
        monsterTest.initBaseAbilities();

        // config mocks
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(request.getRequestDispatcher(JspPath.QUEST)).thenReturn(requestDispatcher);
    }
}
