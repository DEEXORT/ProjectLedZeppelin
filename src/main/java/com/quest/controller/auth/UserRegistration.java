package com.quest.controller.auth;

import com.quest.config.ServiceLocator;
import com.quest.entity.User;
import com.quest.exception.UserAlreadyExistsException;
import com.quest.exception.UserEmptyException;
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
import java.util.ArrayList;

import static com.quest.util.Const.*;
import static com.quest.util.KeyAttribute.PASSWORD;
import static com.quest.util.KeyAttribute.USERNAME;

@WebServlet(ROUTE_REGISTER)
public class UserRegistration extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig config) {
        userService = ServiceLocator.getService(UserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute(KeyAttribute.USER);
        if (user != null) {
            resp.sendRedirect(ROUTE_PROFILE);
        } else {
            req.getRequestDispatcher(PATH_REGISTER_JSP).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(USERNAME);
        String password = req.getParameter(PASSWORD);
        User user = User.builder()
                .login(login)
                .password(password)
                .achievements(new ArrayList<>())
                .build();
        try {
            userService.create(user);
            HttpSession session = req.getSession();
            session.setAttribute(KeyAttribute.USER, user);
            resp.sendRedirect(ROUTE_GAME);
        } catch (UserAlreadyExistsException | UserEmptyException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            req.setAttribute(KeyAttribute.ERROR, e.getMessage());
            req.getRequestDispatcher(PATH_REGISTER_JSP).forward(req, resp);
        }
    }
}
