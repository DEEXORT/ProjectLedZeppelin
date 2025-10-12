package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.entity.Action;
import com.quest.entity.QuestScene;
import com.quest.services.hibernate.HibernateMonsterService;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.services.hibernate.HibernateQuestService;
import com.quest.services.resolver.EventResolver;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class QuestServletIT extends ConfigIT {
    private QuestServlet questServlet;

    @Mock
    HibernateQuestService mockedQuestService;

    @Mock
    HibernateMonsterService mockedMonsterService;

    @Mock
    HibernatePlayerService mockedPlayerService;

    @Mock
    EventResolver mockedEventResolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        questServlet = new QuestServlet(mockedPlayerService, mockedMonsterService, mockedQuestService, mockedEventResolver);
    }

    @Test
    void doGet_ShouldSetQuestAttributesAndForwardToJsp_WhenSceneExists() throws Exception {
        // given
        playerTest.setQuestSceneId(2L);
        Action action = Action.builder().questSceneId(2L).actionText("TestActionText").build();

        QuestScene questScene = QuestScene.builder()
                .id(2L)
                .descriptionScene("TestScene")
                .nameScene("TestScene")
                .actions(new ArrayList<>())
                .build();
        questScene.getActions().add(action);

        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(request.getRequestDispatcher(JspPath.QUEST)).thenReturn(requestDispatcher);
        when(mockedQuestService.get(2L)).thenReturn(Optional.of(questScene));

        // when
        questServlet.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request, response);
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