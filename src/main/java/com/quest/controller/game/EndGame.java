package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.entity.Player;
import com.quest.entity.QuestScene;
import com.quest.entity.User;
import com.quest.services.QuestService;
import com.quest.services.UserService;
import com.quest.util.KeyAttribute;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import static com.quest.util.Const.*;

@WebServlet(ROUTE_END)
public class EndGame extends HttpServlet {
    private QuestService questService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) {
        questService = ServiceLocator.getService(QuestService.class);
        userService = ServiceLocator.getService(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Player player = (Player) session.getAttribute(KeyAttribute.PLAYER);
        User user = (User) session.getAttribute(KeyAttribute.USER);

        // Получение сцены с концовкой и сохранение достижения
        Optional<QuestScene> questScene = questService.get(player.getQuestSceneId());
        questScene.ifPresent(scene -> {
            req.setAttribute(KeyAttribute.QUEST_DESCRIPTION, scene.getDescriptionScene());
            Long questId = scene.getId();
            if (scene.getAchievement() != null) {
                user.getAchievements().add(scene.getAchievement());
                userService.update(user);
            }
            // Если сцена - сюжетная концовка
            if (questId >= ID_END_MIN && questId < ID_END_MAX) {
                req.getSession().setAttribute(
                        KeyAttribute.IMG_END_GAME,
                        RESOURCE_IMG_FINISH);
            }
            // Если игрок погиб
            else if (questId >= ID_END_MIN && questId < ID_DEATH_MAX) {
                req.getSession().setAttribute(
                        KeyAttribute.IMG_END_GAME,
                        RESOURCE_IMG_RIP);
            }
        });

        // Сброс игрока у пользователя
        user.setPlayerId(null);

        req.getRequestDispatcher(PATH_END_GAME_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(ROUTE_GAME);
    }
}
