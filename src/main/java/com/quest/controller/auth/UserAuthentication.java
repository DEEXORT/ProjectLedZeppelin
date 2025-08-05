package com.quest.controller.auth;

import com.quest.config.ServiceLocator;
import com.quest.entity.User;
import com.quest.exception.UserEmptyException;
import com.quest.exception.UserNotFoundException;
import com.quest.services.UserService;
import com.quest.util.KeyAttribute;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import static com.quest.util.Const.*;
import static com.quest.util.KeyAttribute.PASSWORD;
import static com.quest.util.KeyAttribute.USERNAME;

@WebServlet(ROUTE_LOGIN)
public class UserAuthentication extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) {
        userService = ServiceLocator.getService(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Если пользователь есть в сессии, то перейти в профиль
        User user = (User) req.getSession().getAttribute(KeyAttribute.USER); // TODO: Заменить на Object
        if (user != null) {
            resp.sendRedirect(ROUTE_PROFILE);
        } else {
            req.getRequestDispatcher(PATH_LOGIN_JSP).forward(req, resp);
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
                resp.sendRedirect(ROUTE_GAME);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                req.setAttribute(KeyAttribute.ERROR, "Неверный логин или пароль");
                req.getRequestDispatcher(PATH_LOGIN_JSP).forward(req, resp); // TODO: Заменить на redirect
            }
        } catch (UserEmptyException | UserNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            req.setAttribute(KeyAttribute.ERROR, e.getMessage());
            req.getRequestDispatcher(PATH_LOGIN_JSP).forward(req, resp); // TODO: заменить на redirect
        }
    }

}
