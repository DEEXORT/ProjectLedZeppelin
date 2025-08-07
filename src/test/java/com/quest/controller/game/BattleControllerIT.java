package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.entity.Monster;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BattleControllerIT extends ConfigIT {
    private final BattleController battleController = ServiceLocator.getService(BattleController.class);

    @Test
    void doGet_ShouldGenerateMonsterAndForwardToJsp() throws ServletException, IOException {
        // given
        battleController.init(servletConfig);
        when(request.getRequestDispatcher(JspPath.BATTLE)).thenReturn(requestDispatcher);
        when(session.getAttribute(KeyAttribute.MONSTER)).thenReturn(monsterTest);

        // when
        battleController.doGet(request, response);

        // then
        verify(session).setAttribute(eq(KeyAttribute.MONSTER), any(Monster.class));
        verify(request).setAttribute(KeyAttribute.ACTION, "throwDice");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doPost_ShouldRedirectToQuest_WhenMonsterWon() throws Exception {
        // given
        battleController.init(servletConfig);
        when(session.getAttribute(KeyAttribute.MONSTER)).thenReturn(monsterTest);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        CombatResolver combatResolver = ServiceLocator.getService(CombatResolver.class);
        Random mockedRandom = mock(Random.class);
        Field random = combatResolver.getClass().getDeclaredField("random");
        random.set(combatResolver, mockedRandom);
        when(mockedRandom.nextInt(anyInt()))
                .thenReturn(10);  // for player
        when(request.getRequestDispatcher(JspPath.BATTLE)).thenReturn(requestDispatcher);

        // when
        battleController.doPost(request, response);

        // then
        assertEquals(0, playerTest.getHealth());
        verify(response).sendRedirect(Route.QUEST);
    }

    @Test
    void doPost_ShouldUpdateBattleJsp_WhenPlayerWon() throws Exception {
        // given
        battleController.init(servletConfig);
        when(session.getAttribute(KeyAttribute.MONSTER)).thenReturn(monsterTest);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(request.getRequestDispatcher(JspPath.BATTLE)).thenReturn(requestDispatcher);
        CombatResolver combatResolver = ServiceLocator.getService(CombatResolver.class);
        Random mockedRandom = mock(Random.class);
        Field random = combatResolver.getClass().getDeclaredField("random");
        random.set(combatResolver, mockedRandom);
        when(mockedRandom.nextInt(anyInt()))
                .thenReturn(70);  // for player

        // when
        battleController.doPost(request, response);

        // then
        assertEquals(0, monsterTest.getHealth());
        verify(request).setAttribute(KeyAttribute.ACTION, "left");
        verify(requestDispatcher).forward(request, response);
    }
}