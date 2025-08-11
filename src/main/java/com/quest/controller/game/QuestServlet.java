package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.entity.*;
import com.quest.services.MonsterService;
import com.quest.services.PlayerService;
import com.quest.services.QuestService;
import com.quest.util.*;
import jakarta.servlet.ServletConfig;
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
    private PlayerService playerService;
    private MonsterService monsterService;
    private QuestService questService;
    private final Random random = new Random();

    @Override
    public void init(ServletConfig config) {
        playerService = ServiceLocator.getService(PlayerService.class);
        monsterService = ServiceLocator.getService(MonsterService.class);
        questService = ServiceLocator.getService(QuestService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // clear attribute after battle
        req.getSession().removeAttribute(KeyAttribute.MONSTER);

        Optional<QuestScene> questScene = getQuestScene(req);
        if (questScene.isPresent()) {
            // Если сцена - концовка
            Long questId = questScene.get().getId();
            if (questId >= ParseConst.ID_END_MIN && questId < ParseConst.ID_END_MAX) {
                resp.sendRedirect(Route.END);
                return;
            }

            Long monsterId = questScene.get().getMonsterId();
            if (monsterId != null) {
                // Если есть монстр в сцене, то редирект на сражение
                Optional<Monster> monster = monsterService.get(monsterId);
                if (monster.isPresent()) {
                    req.getSession().setAttribute(KeyAttribute.MONSTER, monster.get());

                    // Сохранение id следующей сцены для player
                    List<Action> actions = questScene.get().getActions();
                    updatePlayer(req.getSession(), actions.get(0).getNextQuestSceneId());

                    resp.sendRedirect(Route.BATTLE);
                }
            } else {
                // Запись сцены в атрибуты для отображения в JSP.
                req.getSession().setAttribute(KeyAttribute.BATTLE_FLAG, false);
                setQuestSceneToRequestAttributes(req, questScene.get());
                req.getRequestDispatcher(JspPath.QUEST).forward(req, resp);
            }
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

        if (handleEvent(req, resp, nextQuestSceneId)) return;

        // Save game process to database
        updatePlayer(session, nextQuestSceneId);

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

    private void updatePlayer(HttpSession session, long nextQuestSceneId) {
        Player player = (Player) session.getAttribute(KeyAttribute.PLAYER);
        player.setQuestSceneId(nextQuestSceneId);
        playerService.update(player);
    }

    private void setQuestSceneToRequestAttributes(HttpServletRequest req, QuestScene questScene) throws ServletException, IOException {
        questScene.getActions().forEach(action -> {
            logger.debug("QuestScene found. Actions: {}", action.getActionText());
        });
        req.setAttribute(KeyAttribute.QUEST_DESCRIPTION, questScene.getDescriptionScene());
        req.setAttribute(KeyAttribute.QUEST_ACTIONS, questScene.getActions());
    }

    private Optional<QuestScene> getQuestScene(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Player player = (Player) session.getAttribute(KeyAttribute.PLAYER);
        QuestService questService = ServiceLocator.getService(QuestService.class);
        return questService.get(player.getQuestSceneId());
    }

    private boolean handleEvent(HttpServletRequest req, HttpServletResponse resp, long nextQuestSceneId) throws IOException {
        HttpSession session = req.getSession();
        Object attribute = session.getAttribute(KeyAttribute.EVENT);
        Player player = (Player) session.getAttribute(KeyAttribute.PLAYER);

        if (attribute != null) {
            Event event = (Event) attribute;
            switch (event.getType()) {
                case DAMAGE -> {
                    // Change player stats
                    player.setHealth(player.getHealth() - event.getValue());
                    // Перезапись атрибута QUEST_DESCRIPTION с описанием полученного урона
                    String questDescription = MessageBundle.get("quest.damage").formatted(event.getValue());
                    session.setAttribute(KeyAttribute.QUEST_DESCRIPTION, questDescription);
                    // Перезапись атрибута QUEST_ACTIONS (только кнопка "Дальше")
                    Collection<Action> actions = new ArrayList<>();
                    actions.add(Action.builder()
                            .actionText(MessageBundle.get("quest.next"))
                            .nextQuestSceneId(nextQuestSceneId)
                            .build());
                    session.setAttribute(KeyAttribute.QUEST_ACTIONS, actions);
                    // Удаление атрибута EVENT
                    session.removeAttribute(KeyAttribute.EVENT);
                    // Редирект на /quest (GET)
                    resp.sendRedirect(Route.QUEST);
                    return true;
                }
                case BUFF -> {}
                case HEAL -> {}
                case DEBUFF -> {}
            }
        }
        return false;
    }
}
