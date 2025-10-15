package com.quest.controller.auth;

import com.quest.config.ServiceLocator;
import com.quest.dto.AbilityTo;
import com.quest.dto.PlayerTo;
import com.quest.services.hibernate.HibernateAbilityService;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.RequestHelper;
import com.quest.util.Route;
import jakarta.servlet.ServletConfig;
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
    private HibernateAbilityService abilityService;
    private HibernatePlayerService playerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        abilityService = ServiceLocator.getService(HibernateAbilityService.class);
        playerService = ServiceLocator.getService(HibernatePlayerService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<AbilityTo> list = abilityService.getAll()
                .stream()
                .sorted(Comparator.comparingInt(AbilityTo::getLevelRequirement))
                .toList();

        req.getSession().setAttribute(KeyAttribute.ALL_ABILITIES, list);
        req.getRequestDispatcher(JspPath.PROFILE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PlayerTo playerTo = RequestHelper.getValueAttr(req, KeyAttribute.PLAYER, PlayerTo.class);
        String[] abilityIds = req.getParameterValues(KeyAttribute.ABILITIES_IDS);

        if (abilityIds != null) {
            playerTo.getAbilities().clear();
            for (String abilityId : abilityIds) {
                Optional<AbilityTo> ability = abilityService.get(Long.parseLong(abilityId));
                ability.ifPresent(value -> playerTo.getAbilities().add(value));
            }
            playerService.update(playerTo);
        }

        resp.sendRedirect(Route.PROFILE);
    }
}
