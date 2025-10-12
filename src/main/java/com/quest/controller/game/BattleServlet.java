package com.quest.controller.game;

import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.entity.BattleHistory;
import com.quest.entity.character.Monster;
import com.quest.entity.character.Player;
import com.quest.services.hibernate.HibernateAbilityService;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.services.hibernate.HibernateQuestService;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

@WebServlet(Route.BATTLE)
public class BattleServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(BattleServlet.class);
    public final HibernateAbilityService abilityService;
    public final HibernatePlayerService playerService;
    private final BattleResolver battleResolver;
    private final HibernateQuestService questService;

    public BattleServlet(HibernateAbilityService abilityService, HibernatePlayerService playerService, BattleResolver battleResolver, HibernateQuestService questService) {
        this.abilityService = abilityService;
        this.playerService = playerService;
        this.battleResolver = battleResolver;
        this.questService = questService;
    }

    public BattleServlet() {
        this(ServiceLocator.getService(HibernateAbilityService.class),
                ServiceLocator.getService(HibernatePlayerService.class),
                ServiceLocator.getService(BattleResolver.class),
                ServiceLocator.getService(HibernateQuestService.class));
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
        Optional<Ability> optionalAbility = abilityService.get(abilityId);
        if (optionalAbility.isPresent()) {
            Ability ability = optionalAbility.get();

            battleResolver.resolveBattle(player, monster, ability, history); // Dealing damage
            req.getSession().setAttribute(KeyAttribute.BATTLE_HISTORY, history); // Update history in session

            if (player.getHealth() <= 0) {
                player.setQuestSceneId(questService.getBattleDeathScene().getId());
                playerService.update(player);
                resp.sendRedirect(Route.QUEST);
            } else {
                resp.sendRedirect(Route.BATTLE);
            }
        } else {
            logger.warn("Ability not found: " + abilityId);
            throw new RuntimeException("Ability not found");
        }
    }

}
