package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.entity.QuestScene;
import com.quest.entity.character.Player;
import com.quest.services.hibernate.HibernateMonsterService;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.services.hibernate.HibernateQuestService;
import com.quest.services.resolver.EventResolver;
import com.quest.services.resolver.QuestResolver;
import com.quest.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;


@WebServlet(Route.QUEST)
public class QuestServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger(QuestServlet.class);
    private final HibernatePlayerService playerService;
    private final HibernateMonsterService monsterService;
    private final HibernateQuestService questService;
    private final EventResolver eventResolver;

    public QuestServlet(HibernatePlayerService playerService, HibernateMonsterService monsterService, HibernateQuestService questService, EventResolver eventResolver) {
        this.playerService = playerService;
        this.monsterService = monsterService;
        this.questService = questService;
        this.eventResolver = eventResolver;
    }

    public QuestServlet() {
        this(ServiceLocator.getService(HibernatePlayerService.class),
                ServiceLocator.getService(HibernateMonsterService.class),
                ServiceLocator.getService(HibernateQuestService.class),
                ServiceLocator.getService(EventResolver.class));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuestResolver questResolver = ServiceLocator.getService(QuestResolver.class);
        // clear attribute after battle
        req.getSession().removeAttribute(KeyAttribute.MONSTER);

        Optional<QuestScene> questScene = getQuestScene(req);
        if (questScene.isPresent()) {
            questResolver.resolve(req, resp, questScene.get());
        } else {
            logger.error("Quest scene not found");
            // TODO: Сделать неожиданную концовку с несчастным случаем :)
            req.setAttribute(KeyAttribute.ERROR, "Quest scene not found");
            req.getRequestDispatcher(JspPath.ERROR).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        long nextQuestSceneId = Long.parseLong(req.getParameter(KeyAttribute.SCENE_ID));
        HttpSession session = req.getSession();

        // hook for event
        if (handleEvent(req, resp)) return;

        // Save game process to database
        saveGameProcess(req, nextQuestSceneId);

        // Possible generation random event of battle
        if (questService.isBattleEvent(req)) {
            // Fail. Redirect to battle
            session.setAttribute(KeyAttribute.MONSTER, monsterService.getRandomMonster());
            session.setAttribute(KeyAttribute.QUEST_DESCRIPTION, ResourceBundleManager.getMessage("quest.description_random_event"));
            resp.sendRedirect(Route.BATTLE);
        }
        // Otherwise redirect to next QuestScene
        else {
            resp.sendRedirect(Route.QUEST);
        }
    }

    private void saveGameProcess(HttpServletRequest req, long nextQuestSceneId) {
        Player player = RequestHelper.getValueAttr(req, KeyAttribute.PLAYER, Player.class);
        player.setQuestSceneId(nextQuestSceneId);
        playerService.update(player);
    }

    @Transactional
    private Optional<QuestScene> getQuestScene(HttpServletRequest req) {
        Player player = RequestHelper.getValueAttr(req, KeyAttribute.PLAYER, Player.class);
        return questService.get(player.getQuestSceneId());
    }

    private boolean handleEvent(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (req.getParameter(KeyAttribute.EVENT_ID) != null) {
            eventResolver.resolve(req, resp);
            return true;
        } else {
            return false;
        }
    }
}
