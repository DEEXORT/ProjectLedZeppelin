package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.entity.*;
import com.quest.services.MonsterService;
import com.quest.services.PlayerService;
import com.quest.services.QuestService;
import com.quest.services.resolver.EventResolver;
import com.quest.services.resolver.QuestResolver;
import com.quest.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;


@WebServlet(Route.QUEST)
public class QuestServlet extends HttpServlet {
    private final Logger logger = LogManager.getLogger(QuestServlet.class);
    private final PlayerService playerService;
    private final MonsterService monsterService;
    private final QuestService questService;

    public QuestServlet(PlayerService playerService, MonsterService monsterService, QuestService questService) {
        this.playerService = playerService;
        this.monsterService = monsterService;
        this.questService = questService;
    }

    public QuestServlet() {
        this(ServiceLocator.getService(PlayerService.class),
                ServiceLocator.getService(MonsterService.class),
                ServiceLocator.getService(QuestService.class));
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
        saveGameProcess(session, nextQuestSceneId);

        // Possible generation random event of battle
        if (questService.isBattleEvent(req)) {
            // Fail. Redirect to battle
            session.setAttribute(KeyAttribute.MONSTER, monsterService.getRandomMonster());
            resp.sendRedirect(Route.BATTLE);
        }
        // Otherwise redirect to next QuestScene
        else {
            resp.sendRedirect(Route.QUEST);
        }
    }

    private void saveGameProcess(HttpSession session, long nextQuestSceneId) {
        Player player = (Player) session.getAttribute(KeyAttribute.PLAYER);
        player.setQuestSceneId(nextQuestSceneId);
        playerService.update(player);
    }

    private Optional<QuestScene> getQuestScene(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Player player = (Player) session.getAttribute(KeyAttribute.PLAYER);
        QuestService questService = ServiceLocator.getService(QuestService.class);
        return questService.get(player.getQuestSceneId());
    }

    private boolean handleEvent(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (req.getParameter(KeyAttribute.EVENT_ID) != null) {
            EventResolver eventResolver = ServiceLocator.getService(EventResolver.class);
            eventResolver.resolve(req, resp);
            return true;
        } else {
            return false;
        }
    }
}
