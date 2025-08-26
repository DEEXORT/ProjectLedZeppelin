package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.entity.QuestScene;
import com.quest.entity.User;
import com.quest.entity.character.Player;
import com.quest.services.QuestService;
import com.quest.services.UserService;
import com.quest.util.*;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet(Route.END)
public class EndGameServlet extends HttpServlet {
    private QuestService questService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) {
        questService = ServiceLocator.getService(QuestService.class);
        userService = ServiceLocator.getService(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Player player = RequestHelper.getValueAttr(req, KeyAttribute.PLAYER, Player.class);
        User user = RequestHelper.getValueAttr(req, KeyAttribute.USER, User.class);

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
            if (questId >= ParseConst.ID_END_MIN && questId < ParseConst.ID_END_MAX) {
                req.getSession().setAttribute(
                        KeyAttribute.IMG_END_GAME,
                        ResourcePath.IMG_FINISH);
            }
            // Если игрок погиб
            else if (questId >= ParseConst.ID_END_MIN && questId < ParseConst.ID_DEATH_MAX) {
                req.getSession().setAttribute(
                        KeyAttribute.IMG_END_GAME,
                        ResourcePath.IMG_RIP);
            }
        });

        // Сброс игрока у пользователя
        user.setPlayerId(null);

        req.getRequestDispatcher(JspPath.END_GAME).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(Route.GAME);
    }
}
