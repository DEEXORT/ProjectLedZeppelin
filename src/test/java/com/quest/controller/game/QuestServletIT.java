package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.services.MonsterService;
import com.quest.services.PlayerService;
import com.quest.services.QuestService;
import com.quest.services.resolver.EventResolver;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Random;

import static org.mockito.Mockito.*;

class QuestServletIT extends ConfigIT {
    private QuestServlet questServlet;

    @Mock
    QuestService mockedQuestService;

    @Mock
    MonsterService mockedMonsterService;

    @Mock
    PlayerService mockedPlayerService;

    @Mock
    EventResolver mockedEventResolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        questServlet = new QuestServlet(mockedPlayerService, mockedMonsterService, mockedQuestService);
    }

    @Test
    void doGet_ShouldSetQuestAttributesAndForwardToJsp_WhenSceneExists() throws Exception {
        // given
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
        when(request.getParameter(KeyAttribute.SCENE_ID)).thenReturn("2");
        when(session.getAttribute(KeyAttribute.BATTLE_FLAG)).thenReturn(false);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(mockedQuestService.isBattleEvent(request)).thenReturn(true);

        // when
        questServlet.doPost(request, response);

        // then
        verify(response).sendRedirect(Route.BATTLE);
    }

    @Test
    void doPost_ShouldRedirectToNextQuest_AfterQuest() throws Exception {
        // given
        when(request.getParameter(KeyAttribute.SCENE_ID)).thenReturn("2");
        when(session.getAttribute(KeyAttribute.BATTLE_FLAG)).thenReturn(false);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(mockedQuestService.isBattleEvent(request)).thenReturn(false);
//        try (MockedStatic<ServiceLocator> serviceLocator = mockStatic(ServiceLocator.class)) {
//            serviceLocator
//                    .when(() -> ServiceLocator.getService(EventResolver.class))
//                    .thenReturn(mockedEventResolver);
//        }

        // when
        questServlet.doPost(request, response);

        // then
        verify(response).sendRedirect(Route.QUEST);
    }

    @Test
    void doPost_ShouldRedirectToNextQuest_AfterBattle() throws Exception {
        // given
        when(request.getParameter(KeyAttribute.SCENE_ID)).thenReturn("2");
        when(session.getAttribute(KeyAttribute.BATTLE_FLAG)).thenReturn(true);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(mockedQuestService.isBattleEvent(request)).thenReturn(false);

        // when
        questServlet.doPost(request, response);

        // then
        verify(response).sendRedirect(Route.QUEST);
    }
}