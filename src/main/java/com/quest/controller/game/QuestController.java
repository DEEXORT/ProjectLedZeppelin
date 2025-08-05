package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.entity.Action;
import com.quest.entity.Monster;
import com.quest.entity.Player;
import com.quest.entity.QuestScene;
import com.quest.services.MonsterService;
import com.quest.services.PlayerService;
import com.quest.services.QuestService;
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
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.quest.util.Const.*;
import static com.quest.util.KeyAttribute.*;

@WebServlet(ROUTE_QUEST)
public class QuestController extends HttpServlet {
    private final Logger logger = LogManager.getLogger(QuestController.class);
    private PlayerService playerService;
    private MonsterService monsterService;
    private final Random random = new Random();

    @Override
    public void init(ServletConfig config) {
        playerService = ServiceLocator.getService(PlayerService.class);
        monsterService = ServiceLocator.getService(MonsterService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Чистка атрибута после сражения
        req.getSession().removeAttribute(MONSTER);

        Optional<QuestScene> questScene = getQuestScene(req);
        if (questScene.isPresent()) {
            // Если сцена - концовка
            Long questId = questScene.get().getId();
            if (questId >= ID_END_MIN && questId < ID_END_MAX) {
                resp.sendRedirect(ROUTE_END);
                return;
            }

            Long monsterId = questScene.get().getMonsterId();
            if (monsterId != null) {
                // Если есть монстр в сцене, то редирект на сражение
                Optional<Monster> monster = monsterService.get(monsterId);
                if (monster.isPresent()) {
                    req.getSession().setAttribute(MONSTER, monster.get());

                    // Сохранение id следующей сцены для player
                    List<Action> actions = questScene.get().getActions();
                    updatePlayer(req.getSession(), actions.get(0).getNextQuestSceneId());

                    resp.sendRedirect(ROUTE_BATTLE);
                }
            } else {
                // Запись сцены в атрибуты для отображения в JSP.
                req.getSession().setAttribute(BATTLE_FLAG, false);
                setQuestSceneToRequestAttributes(req, questScene.get());
                req.getRequestDispatcher(PATH_QUEST_JSP).forward(req, resp);
            }
        } else {
            logger.error("Quest scene not found");
            // TODO: Сделать неожиданную концовку с несчастным случаем :)
            req.setAttribute(ERROR, "Quest scene not found");
            req.getRequestDispatcher(PATH_ERROR_JSP).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        long nextQuestSceneId = Long.parseLong(req.getParameter(SCENE_ID));
        HttpSession session = req.getSession();

        // Сохранение процесса игры (id следующей сцены) в БД
        updatePlayer(session, nextQuestSceneId);

        // Генерация события с монстром. Если повезло, то редирект на след квест
        if (!((boolean) session.getAttribute(BATTLE_FLAG))
                && (random.nextInt(100) < 40)) {
            // Неудача. Сражение с монстром
            setMonster(session);
            resp.sendRedirect(ROUTE_BATTLE);
        }
        // Переход на следующую сцену
        else {
            resp.sendRedirect(ROUTE_QUEST);
        }
    }

    private void updatePlayer(HttpSession session, long nextQuestSceneId) {
        Player player = (Player) session.getAttribute(PLAYER);
        player.setQuestSceneId(nextQuestSceneId);
        playerService.update(player);
    }

    private void setMonster(HttpSession session) {
        // Получение случайного монстра
        Monster monster = monsterService.getRandomMonster();
        monster.setHealth(monster.getMaxHealth()); // TODO: заменить на удаление монстра из сессии
        session.setAttribute(MONSTER, monster);
    }

    private void setQuestSceneToRequestAttributes(HttpServletRequest req, QuestScene questScene) throws ServletException, IOException {
        questScene.getActions().forEach(action -> {
            logger.debug("QuestScene found. Actions: {}", action.getActionText());
        });
        req.setAttribute(QUEST_DESCRIPTION, questScene.getDescriptionScene());
        req.setAttribute(QUEST_ACTIONS, questScene.getActions());
    }

    private static Optional<QuestScene> getQuestScene(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Player player = (Player) session.getAttribute(PLAYER);
        QuestService questService = ServiceLocator.getService(QuestService.class);
        return questService.get(player.getQuestSceneId());
    }

}
