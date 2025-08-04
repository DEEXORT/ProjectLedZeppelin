package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.util.KeyAttribute;
import com.quest.util.Const;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Random;

import static com.quest.util.Const.PATH_QUEST_JSP;
import static org.mockito.Mockito.*;

class QuestControllerIT extends ConfigIT {
    private final QuestController questController = ServiceLocator.getService(QuestController.class);

    // Тестирование GET-запросов
    @Test
    void doGet_ShouldSetQuestAttributesAndForwardToJsp_WhenSceneExists() throws Exception {
        // given
        questController.init(servletConfig);
        playerTest.setQuestSceneId(2L);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(request.getRequestDispatcher(PATH_QUEST_JSP)).thenReturn(dispatcher);

        // when
        questController.doGet(request, response);

        // then
        verify(dispatcher).forward(request, response);
    }

    // Тестирование POST-запросов
    @Test
    void doPost_ShouldRedirectToBattle_AfterQuest() throws Exception {
        // given
        questController.init(servletConfig);
        when(request.getParameter(KeyAttribute.SCENE_ID)).thenReturn("2");
        when(session.getAttribute(KeyAttribute.BATTLE_FLAG)).thenReturn(false);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        // Подмена рандома (принудительное сражение) через рефлексию
        Random mockedRandom = mock(Random.class);
        when(mockedRandom.nextInt(100)).thenReturn(49);
        Field randomField = QuestController.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(questController, mockedRandom);

        // when
        questController.doPost(request, response);

        // then
        verify(response).sendRedirect(Const.ROUTE_QUEST);
    }

    @Test
    void doPost_ShouldRedirectToNextQuest_AfterQuest() throws Exception {
        // given
        questController.init(servletConfig);
        when(request.getParameter(KeyAttribute.SCENE_ID)).thenReturn("2");
        when(session.getAttribute(KeyAttribute.BATTLE_FLAG)).thenReturn(false);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        // Подмена рандома (принудительное сражение) через рефлексию
        Random mockedRandom = mock(Random.class);
        when(mockedRandom.nextInt(100)).thenReturn(70);
        Field randomField = QuestController.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(questController, mockedRandom);

        // when
        questController.doPost(request, response);

        // then
        verify(response).sendRedirect(Const.ROUTE_QUEST);
    }

    @Test
    void doPost_ShouldRedirectToNextQuest_AfterBattle() throws Exception {
        // given
        questController.init(servletConfig);
        when(request.getParameter(KeyAttribute.SCENE_ID)).thenReturn("2");
        when(session.getAttribute(KeyAttribute.BATTLE_FLAG)).thenReturn(true);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);

        // when
        questController.doPost(request, response);

        // then
        verify(response).sendRedirect(Const.ROUTE_QUEST);
    }
}