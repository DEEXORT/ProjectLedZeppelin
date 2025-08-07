package com.quest.filters;

import com.quest.entity.Player;
import com.quest.util.KeyAttribute;
import com.quest.util.ResourcePath;
import com.quest.util.Route;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebFilter({Route.QUEST})
public class HealthPlayerFilter extends HttpFilter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();

        Player player = (Player) session.getAttribute(KeyAttribute.PLAYER);

        if (player != null && player.getHealth() <= 0) {
            player.setQuestSceneId(951L);
            session.setAttribute(KeyAttribute.IMG_END_GAME, ResourcePath.IMG_RIP);
            response.sendRedirect(request.getContextPath() + Route.END);
        } else {
            chain.doFilter(req, res);
        }
    }
}
