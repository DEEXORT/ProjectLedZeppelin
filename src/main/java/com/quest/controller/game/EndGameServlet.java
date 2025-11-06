package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.dto.PlayerTo;
import com.quest.dto.QuestSceneTo;
import com.quest.dto.UserTo;
import com.quest.entity.QuestSceneType;
import com.quest.services.hibernate.HibernateQuestService;
import com.quest.services.hibernate.HibernateUserService;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.RequestHelper;
import com.quest.util.ResourcePath;
import com.quest.util.Route;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(Route.END)
public class EndGameServlet extends HttpServlet {
    private HibernateQuestService questService;
    private HibernateUserService userService;

    @Override
    public void init(ServletConfig config) {
        questService = ServiceLocator.getService(HibernateQuestService.class);
        userService = ServiceLocator.getService(HibernateUserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PlayerTo player = RequestHelper.getValueAttr(req, KeyAttribute.PLAYER, PlayerTo.class);
        UserTo user = RequestHelper.getValueAttr(req, KeyAttribute.USER, UserTo.class);

        // Getting the ending quest scene and saving the achievement for player
        QuestSceneTo questScene = questService.get(player.getQuestSceneId());
        if (questScene != null) {
            req.setAttribute(KeyAttribute.QUEST_DESCRIPTION, questScene.getDescriptionScene());
            if (questScene.getAchievement() != null) {
                userService.update(user);
            }
            // If the scene with the story ending
            if (questScene.getType() == QuestSceneType.COMPLETE) {
                req.getSession().setAttribute(
                        KeyAttribute.IMG_END_GAME,
                        ResourcePath.IMG_FINISH);
            }
            // If the player died
            else if (questScene.getType() == QuestSceneType.DEATH
                    || questScene.getType() == QuestSceneType.BATTLE_DEATH) {
                req.getSession().setAttribute(
                        KeyAttribute.IMG_END_GAME,
                        ResourcePath.IMG_RIP);
            }
        }

        // Resetting player for user
        user.setCharacterId(null);

        req.getRequestDispatcher(JspPath.END_GAME).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(Route.GAME);
    }
}
