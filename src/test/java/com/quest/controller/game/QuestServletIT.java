package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Random;

import static org.mockito.Mockito.*;

class QuestServletIT extends ConfigIT {
    private final QuestServlet questServlet = ServiceLocator.getService(QuestServlet.class);

    // Тестирование GET-запросов
    @Test
    void doGet_ShouldSetQuestAttributesAndForwardToJsp_WhenSceneExists() throws Exception {
        // given
        questServlet.init(servletConfig);
        playerTest.setQuestSceneId(2L);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(request.getRequestDispatcher(JspPath.QUEST)).thenReturn(dispatcher);

        // when
        questServlet.doGet(request, response);

        // then
        verify(dispatcher).forward(request, response);
    }

    // Тестирование POST-запросов
    @Test
    void doPost_ShouldRedirectToBattle_AfterQuest() throws Exception {
        // given
        questServlet.init(servletConfig);
        when(request.getParameter(KeyAttribute.SCENE_ID)).thenReturn("2");
        when(session.getAttribute(KeyAttribute.BATTLE_FLAG)).thenReturn(false);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        // Подмена рандома (принудительное сражение) через рефлексию
        Random mockedRandom = mock(Random.class);
        when(mockedRandom.nextInt(100)).thenReturn(49);
        Field randomField = QuestServlet.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(questServlet, mockedRandom);

        // when
        questServlet.doPost(request, response);

        // then
        verify(response).sendRedirect(Route.QUEST);
    }

    @Test
    void doPost_ShouldRedirectToNextQuest_AfterQuest() throws Exception {
        // given
        questServlet.init(servletConfig);
        when(request.getParameter(KeyAttribute.SCENE_ID)).thenReturn("2");
        when(session.getAttribute(KeyAttribute.BATTLE_FLAG)).thenReturn(false);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        // Подмена рандома (принудительное сражение) через рефлексию
        Random mockedRandom = mock(Random.class);
        when(mockedRandom.nextInt(100)).thenReturn(70);
        Field randomField = QuestServlet.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(questServlet, mockedRandom);

        // when
        questServlet.doPost(request, response);

        // then
        verify(response).sendRedirect(Route.QUEST);
    }

    @Test
    void doPost_ShouldRedirectToNextQuest_AfterBattle() throws Exception {
        // given
        questServlet.init(servletConfig);
        when(request.getParameter(KeyAttribute.SCENE_ID)).thenReturn("2");
        when(session.getAttribute(KeyAttribute.BATTLE_FLAG)).thenReturn(true);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);

        // when
        questServlet.doPost(request, response);

        // then
        verify(response).sendRedirect(Route.QUEST);
    }
}