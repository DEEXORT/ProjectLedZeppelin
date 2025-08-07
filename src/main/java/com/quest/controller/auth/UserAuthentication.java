package com.quest.controller.auth;

import com.quest.config.ServiceLocator;
import com.quest.entity.User;
import com.quest.exception.UserEmptyException;
import com.quest.exception.UserNotFoundException;
import com.quest.services.UserService;
import com.quest.util.*;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import static com.quest.util.KeyAttribute.PASSWORD;
import static com.quest.util.KeyAttribute.USERNAME;

@WebServlet(Route.LOGIN)
public class UserAuthentication extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) {
        userService = ServiceLocator.getService(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // If user is in the session, then redirect to profile
        Object user = req.getSession().getAttribute(KeyAttribute.USER);
        if (user != null) {
            resp.sendRedirect(Route.PROFILE);
        } else {
            req.getRequestDispatcher(JspPath.LOGIN).forward(req, resp);
            req.getSession().removeAttribute(KeyAttribute.ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(USERNAME);
        String password = req.getParameter(PASSWORD);
        try {
            Optional<User> user = userService.get(login, password);
            if (user.isPresent()) {
                HttpSession session = req.getSession();
                session.setAttribute(KeyAttribute.USER, user.get());
                resp.sendRedirect(Route.GAME);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                req.getSession().setAttribute(KeyAttribute.ERROR, MessageBundle.get("error.invalid_credentials"));
                resp.sendRedirect(Route.LOGIN);
            }
        } catch (UserEmptyException | UserNotFoundException e) {
            RequestHelper.createAuthorizationError(req, resp, e.getMessage());
        }
    }
}
