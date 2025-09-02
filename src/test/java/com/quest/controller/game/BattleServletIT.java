package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.entity.BattleHistory;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BattleServletIT extends ConfigIT {
    private final BattleServlet battleServlet = ServiceLocator.getService(BattleServlet.class);

    @Test
    void doGet_ShouldForwardToJsp() throws ServletException, IOException {
        // given
        battleServlet.init(servletConfig);
        when(request.getRequestDispatcher(JspPath.BATTLE)).thenReturn(requestDispatcher);
        when(session.getAttribute(KeyAttribute.MONSTER)).thenReturn(monsterTest);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(session.getAttribute(KeyAttribute.BATTLE_FLAG)).thenReturn(false);

        // when
        battleServlet.doGet(request, response);

        // then
        verify(session).setAttribute(KeyAttribute.BATTLE_FLAG, true);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doPost_ShouldRedirectToQuest_WhenMonsterWon() throws Exception {
        // given
//        battleServlet.init(servletConfig);
        Ability baseAttack = playerTest.getBaseAttack();
        when(request.getParameter(KeyAttribute.ABILITY_ID)).thenReturn(String.valueOf(baseAttack.getId()));
        when(session.getAttribute(KeyAttribute.MONSTER)).thenReturn(monsterTest);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(request.getRequestDispatcher(JspPath.BATTLE)).thenReturn(requestDispatcher);
        when(session.getAttribute(KeyAttribute.BATTLE_HISTORY)).thenReturn(new BattleHistory());
        playerTest.setHealth(1);
        monsterTest.setHealth(100000);

        // when
        battleServlet.doPost(request, response);

        // then
        assertEquals(0, playerTest.getHealth());
        verify(response).sendRedirect(Route.QUEST);
    }

    @Test
    void doPost_ShouldUpdateBattleJsp_WhenPlayerWon() throws Exception {
        // given
//        battleServlet.init(servletConfig);
        Ability baseAttack = playerTest.getBaseAttack();
        when(request.getParameter(KeyAttribute.ABILITY_ID)).thenReturn(String.valueOf(baseAttack.getId()));
        when(session.getAttribute(KeyAttribute.MONSTER)).thenReturn(monsterTest);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(session.getAttribute(KeyAttribute.BATTLE_HISTORY)).thenReturn(new BattleHistory());
        when(request.getRequestDispatcher(JspPath.BATTLE)).thenReturn(requestDispatcher);
        playerTest.setHealth(1000000);
        monsterTest.setHealth(1);

        //when
        battleServlet.doPost(request, response);

        // then
        assertEquals(0, monsterTest.getHealth());
        verify(response).sendRedirect(Route.BATTLE);
    }
}