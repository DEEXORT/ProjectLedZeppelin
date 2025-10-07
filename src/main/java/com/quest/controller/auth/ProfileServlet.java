package com.quest.controller.auth;

import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.entity.character.Player;
import com.quest.services.AbilityService;
import com.quest.services.PlayerService;
import com.quest.util.KeyAttribute;
import com.quest.util.RequestHelper;
import com.quest.util.Route;
import com.quest.util.JspPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@WebServlet(Route.PROFILE)
public class ProfileServlet extends HttpServlet {
    private final AbilityService abilityService;
    private final PlayerService playerService;

    public ProfileServlet(AbilityService abilityService, PlayerService playerService) {
        this.abilityService = abilityService;
        this.playerService = playerService;
    }

    public ProfileServlet() {
        this(ServiceLocator.getService(AbilityService.class),
                ServiceLocator.getService(PlayerService.class));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Ability> list = abilityService.getAll()
                .stream()
                .sorted(Comparator.comparingInt(Ability::getLevelRequirement))
                .toList();
        req.getSession().setAttribute(KeyAttribute.ALL_ABILITIES, list);
        req.getRequestDispatcher(JspPath.PROFILE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Player player = RequestHelper.getValueAttr(req, KeyAttribute.PLAYER, Player.class);
        String[] abilityIds = req.getParameterValues(KeyAttribute.ABILITIES_IDS);
        if (abilityIds != null) {
            player.getAbilities().clear();
            for (String abilityId : abilityIds) {
                Ability ability = abilityService.getAbility(Long.parseLong(abilityId));
                player.getAbilities().add(ability);
            }
            playerService.update(player);
        }
        resp.sendRedirect(Route.PROFILE);
    }
}
