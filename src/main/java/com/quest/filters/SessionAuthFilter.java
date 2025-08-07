package com.quest.filters;

import com.quest.util.KeyAttribute;
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

@WebFilter({Route.QUEST, Route.GAME, Route.PROFILE, Route.LOGOUT})
public class SessionAuthFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        // Если пользователь есть, допускаем переход
        if (session != null && session.getAttribute(KeyAttribute.USER) != null) {
            chain.doFilter(req, res);
        } else {
            // Иначе просим залогиниться
            response.sendRedirect(Route.LOGIN);
        }
    }
}
