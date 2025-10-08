package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.entity.character.Player;
import com.quest.entity.User;
import com.quest.services.PlayerService;
import com.quest.util.KeyAttribute;
import com.quest.util.RequestHelper;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;


@WebServlet(Route.GAME)
public class GameServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(GameServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Проверка аутентификации пользователя
        HttpSession session = req.getSession();
        User user = RequestHelper.getValueAttr(req, KeyAttribute.USER, User.class);

        // Получить текущее состояние игры из репозитория или начать новую игру
        PlayerService playerService = ServiceLocator.getService(PlayerService.class);
        Player player = null;
        if (user.getPlayerId() == null) {
            // New game
            player = Player.builder()
                    .health(100)
                    .maxHealth(100)
                    .level(1)
                    .attack(10)
                    .questSceneId(1L)
                    .userId(user.getId())
                    .name(user.getLogin())
                    .build();
            playerService.create(player);
            user.setPlayerId(player.getId());
        } else {
            // Continue game
            Optional<Player> optionalPlayer = playerService.get(user.getPlayerId());
            if (optionalPlayer.isPresent()) {
                player = optionalPlayer.get();
            } else {
                logger.warn("Player not found");
            }
        }

        session.setAttribute(KeyAttribute.PLAYER, player);
        session.setAttribute(KeyAttribute.BATTLE_FLAG, false);
        resp.sendRedirect(Route.QUEST);
    }

}
