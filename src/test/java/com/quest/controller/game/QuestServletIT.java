package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.dto.ActionTo;
import com.quest.dto.QuestSceneTo;
import com.quest.services.hibernate.HibernateMonsterService;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.services.hibernate.HibernateQuestService;
import com.quest.services.resolver.EventResolver;
import com.quest.services.resolver.QuestResolver;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestServletIT extends ConfigIT {
//    private MockedStatic<ServiceLocator> serviceLocator;

    @Mock
    HibernateQuestService mockedQuestService;

    @Mock
    HibernateMonsterService mockedMonsterService;

    @Mock
    HibernatePlayerService mockedPlayerService;

    @Mock
    EventResolver mockedEventResolver;

    @Mock
    QuestResolver mockedQuestResolver;

    @InjectMocks
    private QuestServlet questServlet;

//    @BeforeEach
//    void setUp() throws ServletException {
//        serviceLocator = Mockito.mockStatic(ServiceLocator.class);
//        serviceLocator.when(() -> ServiceLocator.getService(HibernateQuestService.class))
//                .thenReturn(mockedQuestService);
//        serviceLocator.when(() -> ServiceLocator.getService(HibernateMonsterService.class))
//                .thenReturn(mockedMonsterService);
//        serviceLocator.when(() -> ServiceLocator.getService(HibernatePlayerService.class))
//                .thenReturn(mockedPlayerService);
//        serviceLocator.when(() -> ServiceLocator.getService(EventResolver.class))
//                .thenReturn(mockedEventResolver);
//        serviceLocator.when(() -> ServiceLocator.getService(QuestResolver.class))
//                .thenReturn(mockedQuestResolver);
//
//        questServlet = new QuestServlet();
//        questServlet.init(servletConfig);
//    }
//
//    @AfterEach
//    void tearDown() {
//        serviceLocator.close();
//    }

    @Test
    void doGet_ShouldSetQuestAttributesAndForwardToJsp_WhenSceneExists() throws Exception {
        // given
        playerTest.setQuestSceneId(2L);
        ActionTo action = ActionTo.builder().actionText("TestActionText").build();

        QuestSceneTo questScene = QuestSceneTo.builder()
                .id(2L)
                .descriptionScene("TestScene")
                .nameScene("TestScene")
                .actions(new ArrayList<>())
                .build();
        questScene.getActions().add(action);
        when(mockedQuestService.get(questScene.getId())).thenReturn(questScene);

        // when
        questServlet.doGet(request, response);

        // then
        verify(mockedQuestResolver).resolve(request, response, questScene);
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