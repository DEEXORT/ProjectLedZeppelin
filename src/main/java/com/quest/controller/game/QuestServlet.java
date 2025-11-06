package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.dto.PlayerTo;
import com.quest.dto.QuestSceneTo;
import com.quest.services.hibernate.HibernateMonsterService;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.services.hibernate.HibernateQuestService;
import com.quest.services.resolver.EventResolver;
import com.quest.services.resolver.QuestResolver;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.RequestHelper;
import com.quest.util.ResourceBundleManager;
import com.quest.util.Route;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@WebServlet(Route.QUEST)
@Slf4j
public class QuestServlet extends HttpServlet {
    private HibernatePlayerService playerService;
    private HibernateMonsterService monsterService;
    private HibernateQuestService questService;
    private EventResolver eventResolver;
    private QuestResolver questResolver;

    @Override
    public void init(ServletConfig config) throws ServletException {
        playerService = ServiceLocator.getService(HibernatePlayerService.class);
        monsterService = ServiceLocator.getService(HibernateMonsterService.class);
        questService = ServiceLocator.getService(HibernateQuestService.class);
        eventResolver = ServiceLocator.getService(EventResolver.class);
        questResolver = ServiceLocator.getService(QuestResolver.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // clear attribute after battle
        req.getSession().removeAttribute(KeyAttribute.MONSTER);

        QuestSceneTo questScene = getQuestScene(req);
        if (questScene != null) {
            questResolver.resolve(req, resp, questScene);
        } else {
            log.error("Quest scene not found");
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
        PlayerTo player = RequestHelper.getValueAttr(req, KeyAttribute.PLAYER, PlayerTo.class);
        player.setQuestSceneId(nextQuestSceneId);
        // Update object in database
        playerService.update(player);
        // Update object in session
        req.getSession().setAttribute(KeyAttribute.PLAYER, player);
    }

    private QuestSceneTo getQuestScene(HttpServletRequest req) {
        PlayerTo player = RequestHelper.getValueAttr(req, KeyAttribute.PLAYER, PlayerTo.class);
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
