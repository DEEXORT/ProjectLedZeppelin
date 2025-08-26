package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.entity.character.Monster;
import com.quest.entity.character.Player;
import com.quest.services.AbilityService;
import com.quest.services.resolver.BattleResolver;
import com.quest.util.JspPath;
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

@WebServlet(Route.BATTLE)
public class BattleServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(BattleServlet.class);
    public final AbilityService abilityService;

    public BattleServlet(AbilityService abilityService) {
        this.abilityService = abilityService;
    }

    public BattleServlet() {
        this(ServiceLocator.getService(AbilityService.class));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.setAttribute(KeyAttribute.BATTLE_FLAG, true);
        req.getRequestDispatcher(JspPath.BATTLE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Monster monster = RequestHelper.getValueAttr(req, KeyAttribute.MONSTER, Monster.class);
        Player player = RequestHelper.getValueAttr(req, KeyAttribute.PLAYER, Player.class);
        long abilityId = Long.parseLong(req.getParameter(KeyAttribute.ABILITY_ID));
        Ability ability = abilityService.get(abilityId);

        // Dealing damage
        BattleResolver resolver = ServiceLocator.getService(BattleResolver.class);
        resolver.resolveBattle(player, monster, ability);

        if (player.getHealth() <= 0) {
            player.setQuestSceneId(991L);
            resp.sendRedirect(Route.QUEST);
        }
        else {
            resp.sendRedirect(Route.BATTLE);
        }
    }
}
