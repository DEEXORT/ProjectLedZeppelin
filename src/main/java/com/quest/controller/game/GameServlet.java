package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.dto.PlayerTo;
import com.quest.dto.QuestSceneTo;
import com.quest.dto.UserTo;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.services.hibernate.HibernateQuestService;
import com.quest.services.hibernate.HibernateUserService;
import com.quest.util.KeyAttribute;
import com.quest.util.RequestHelper;
import com.quest.util.Route;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebServlet(Route.GAME)
public class GameServlet extends HttpServlet {
    private  HibernatePlayerService playerService;
    private  HibernateQuestService questService;
    private  HibernateUserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        playerService = ServiceLocator.getService(HibernatePlayerService.class);
        questService = ServiceLocator.getService(HibernateQuestService.class);
        userService = ServiceLocator.getService(HibernateUserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Checking user authentication
        HttpSession session = req.getSession();
        UserTo user = RequestHelper.getValueAttr(req, KeyAttribute.USER, UserTo.class);
        QuestSceneTo scene = questService.getFirstScene();

        // Getting current game state from repository or start a new game
        PlayerTo player = null;
        if (user.getCharacterId() == null) {
            // New game
            player = PlayerTo.builder()
                    .health(100)
                    .maxHealth(100)
                    .level(1)
                    .attack(10)
                    .questSceneId(scene.getId())
                    .userId(user.getId())
                    .name(user.getLogin())
                    .build();
            player.initBaseAbilities();
            playerService.create(player);
            user.setCharacterId(player.getId());
            userService.update(user);
        } else {
            // Continue game
            player = playerService.get(user.getCharacterId());
        }

        session.setAttribute(KeyAttribute.PLAYER, player);
        session.setAttribute(KeyAttribute.BATTLE_FLAG, false);
        resp.sendRedirect(Route.QUEST);
    }

}
