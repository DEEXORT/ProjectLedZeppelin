package com.quest.controller.auth;

import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.entity.character.Player;
import com.quest.services.hibernate.HibernateAbilityService;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.RequestHelper;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@WebServlet(Route.PROFILE)
public class ProfileServlet extends HttpServlet {
    private final HibernateAbilityService abilityService;
    private final HibernatePlayerService playerService;

    public ProfileServlet(HibernateAbilityService abilityService, HibernatePlayerService playerService) {
        this.abilityService = abilityService;
        this.playerService = playerService;
    }

    public ProfileServlet() {
        this(ServiceLocator.getService(HibernateAbilityService.class),
                ServiceLocator.getService(HibernatePlayerService.class));
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
                Optional<Ability> ability = abilityService.get(Long.parseLong(abilityId));
                ability.ifPresent(value -> player.getAbilities().add(value));
            }
            playerService.update(player);
        }
        resp.sendRedirect(Route.PROFILE);
    }
}
