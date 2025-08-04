package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.entity.Monster;
import com.quest.entity.Player;
import com.quest.util.KeyAttribute;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.quest.util.Const.*;

@WebServlet(ROUTE_BATTLE)
public class BattleController extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(BattleController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        // Флаг для предотвращения повторного сражения
        session.setAttribute(KeyAttribute.BATTLE_FLAG, true);
        // Получение монстра из сессии
        Monster monster = (Monster) session.getAttribute(KeyAttribute.MONSTER);
        monster.setHealth(monster.getMaxHealth());
        session.setAttribute(KeyAttribute.MONSTER, monster);

        req.setAttribute(KeyAttribute.ACTION, "throwDice");
        req.getRequestDispatcher(PATH_BATTLE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Monster monster = (Monster) req.getSession().getAttribute(KeyAttribute.MONSTER);
        Player player = (Player) req.getSession().getAttribute(KeyAttribute.PLAYER);

        // Имитация сражения
        CombatResolver resolver = ServiceLocator.getService(CombatResolver.class);
        resolver.resolveCombat(player, monster);

        if (player.getHealth() <= 0) {
            player.setQuestSceneId(991L);
            resp.sendRedirect(ROUTE_QUEST);
        } else {
            req.setAttribute(KeyAttribute.ACTION, "left");
            req.getRequestDispatcher(PATH_BATTLE_JSP).forward(req, resp);
        }
    }
}
