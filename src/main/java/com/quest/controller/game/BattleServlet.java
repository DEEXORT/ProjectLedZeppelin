package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.entity.BattleHistory;
import com.quest.entity.character.Monster;
import com.quest.entity.character.Player;
import com.quest.repository.Repository;
import com.quest.services.AbilityService;
import com.quest.services.PlayerService;
import com.quest.services.resolver.BattleResolver;
import com.quest.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@WebServlet(Route.BATTLE)
public class BattleServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(BattleServlet.class);
    public final AbilityService abilityService;
    public final PlayerService playerService;
    private final BattleResolver battleResolver;

    public BattleServlet(AbilityService abilityService, PlayerService playerService, BattleResolver battleResolver) {
        this.abilityService = abilityService;
        this.playerService = playerService;
        this.battleResolver = battleResolver;
    }

    public BattleServlet() {
        this(ServiceLocator.getService(AbilityService.class),
                ServiceLocator.getService(PlayerService.class),
                ServiceLocator.getService(BattleResolver.class));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Monster monster = RequestHelper.getValueAttr(req, KeyAttribute.MONSTER, Monster.class);
        Player player = RequestHelper.getValueAttr(req, KeyAttribute.PLAYER, Player.class);

        if (!RequestHelper.getValueAttr(req, KeyAttribute.BATTLE_FLAG, Boolean.class)) {
            // Start battle. Clear battle history
            req.getSession().setAttribute(KeyAttribute.BATTLE_HISTORY, new BattleHistory());
        }

        battleResolver.handleMonsterDefeat(player, monster);

        req.getSession().setAttribute(KeyAttribute.BATTLE_FLAG, true);
        req.getRequestDispatcher(JspPath.BATTLE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Monster monster = RequestHelper.getValueAttr(req, KeyAttribute.MONSTER, Monster.class);
        Player player = RequestHelper.getValueAttr(req, KeyAttribute.PLAYER, Player.class);
        BattleHistory history = RequestHelper.getValueAttr(req, KeyAttribute.BATTLE_HISTORY, BattleHistory.class);
        long abilityId = Long.parseLong(req.getParameter(KeyAttribute.ABILITY_ID));
        Ability ability = abilityService.get(abilityId);

        battleResolver.resolveBattle(player, monster, ability, history); // Dealing damage
        req.getSession().setAttribute(KeyAttribute.BATTLE_HISTORY, history); // Update history in session

        if (player.getHealth() <= 0) {
            player.setQuestSceneId(991L);
            resp.sendRedirect(Route.QUEST);
        }
        else {
            resp.sendRedirect(Route.BATTLE);
        }
    }

}
