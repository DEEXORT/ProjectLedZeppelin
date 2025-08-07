package com.quest.controller.auth;

import com.quest.config.ServiceLocator;
import com.quest.entity.User;
import com.quest.exception.UserAlreadyExistsException;
import com.quest.exception.UserEmptyException;
import com.quest.services.UserService;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(Route.REGISTER)
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
            resp.sendRedirect(Route.PROFILE);
        } else {
            req.getRequestDispatcher(JspPath.REGISTER).forward(req, resp);
            req.getSession().removeAttribute(KeyAttribute.ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(KeyAttribute.USERNAME);
        String password = req.getParameter(KeyAttribute.PASSWORD);
        User user = User.builder()
                .login(login)
                .password(password)
                .achievements(new ArrayList<>())
                .build();
        try {
            userService.create(user);
            HttpSession session = req.getSession();
            session.setAttribute(KeyAttribute.USER, user);
            resp.sendRedirect(Route.GAME);
        } catch (UserAlreadyExistsException | UserEmptyException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            req.getSession().setAttribute(KeyAttribute.ERROR, e.getMessage());
            resp.sendRedirect(Route.REGISTER);
        }
    }
}
