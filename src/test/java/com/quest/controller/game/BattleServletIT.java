package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.entity.character.Monster;
import com.quest.services.resolver.BattleResolver;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BattleServletIT extends ConfigIT {
    private final BattleServlet battleServlet = ServiceLocator.getService(BattleServlet.class);

    @Mock
    private MockedStatic<ServiceLocator> mockedServiceLocator;

    @Mock
    private ThreadLocalRandom mockedThreadLocalRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        mockedServiceLocator.close();
    }

    @Test
    void doGet_ShouldGenerateMonsterAndForwardToJsp() throws ServletException, IOException {
        // given
        battleServlet.init(servletConfig);
        when(request.getRequestDispatcher(JspPath.BATTLE)).thenReturn(requestDispatcher);
        when(session.getAttribute(KeyAttribute.MONSTER)).thenReturn(monsterTest);

        // when
        battleServlet.doGet(request, response);

        // then
        verify(session).setAttribute(eq(KeyAttribute.MONSTER), any(Monster.class));
        verify(request).setAttribute(KeyAttribute.ACTION, "throwDice");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doPost_ShouldRedirectToQuest_WhenMonsterWon() throws Exception {
        // given
        battleServlet.init(servletConfig);
        when(session.getAttribute(KeyAttribute.MONSTER)).thenReturn(monsterTest);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(request.getRequestDispatcher(JspPath.BATTLE)).thenReturn(requestDispatcher);
        when(mockedThreadLocalRandom.nextInt(100)).thenReturn(1);
        mockedServiceLocator
                .when(() -> ServiceLocator.getService(BattleResolver.class))
                .thenReturn(new BattleResolver(mockedThreadLocalRandom));

        // when
        battleServlet.doPost(request, response);

        // then
        assertEquals(0, playerTest.getHealth());
        verify(response).sendRedirect(Route.QUEST);
    }

    @Test
    void doPost_ShouldUpdateBattleJsp_WhenPlayerWon() throws Exception {
        // given
        battleServlet.init(servletConfig);
        when(session.getAttribute(KeyAttribute.MONSTER)).thenReturn(monsterTest);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(request.getRequestDispatcher(JspPath.BATTLE)).thenReturn(requestDispatcher);
        when(mockedThreadLocalRandom.nextInt(100)).thenReturn(100);
        mockedServiceLocator
                .when(() -> ServiceLocator.getService(BattleResolver.class))
                .thenReturn(new BattleResolver(mockedThreadLocalRandom));

        // when
        battleServlet.doPost(request, response);

        // then
        assertEquals(0, monsterTest.getHealth());
        verify(request).setAttribute(KeyAttribute.ACTION, "left");
        verify(requestDispatcher).forward(request, response);
    }
}