package com.quest.controller.auth;

import com.quest.config.ServiceLocator;
import com.quest.dto.UserTo;
import com.quest.exception.UserAlreadyExistsException;
import com.quest.exception.UserEmptyException;
import com.quest.services.hibernate.HibernateUserService;
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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(Route.REGISTER)
public class UserRegistrationServlet extends HttpServlet {
    private HibernateUserService userService;

    @Override
    public void init(ServletConfig config) {
        userService = ServiceLocator.getService(HibernateUserService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // If user is in the session, then redirect to profile
        if (userService.isGuest(req)) {
            req.getRequestDispatcher(JspPath.REGISTER).forward(req, resp);
            req.getSession().removeAttribute(KeyAttribute.ERROR);
        } else {
            resp.sendRedirect(Route.PROFILE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(KeyAttribute.USERNAME);
        String password = req.getParameter(KeyAttribute.PASSWORD);
        UserTo user = UserTo.builder()
                .login(login)
                .password(password)
                .build();
        try {
            userService.create(user);
            HttpSession session = req.getSession();
            session.setAttribute(KeyAttribute.USER, user);
            resp.sendRedirect(Route.GAME);
        } catch (UserAlreadyExistsException | UserEmptyException e) {
            RequestHelper.createAuthorizationError(req, resp, e.getMessage());
        }
    }
}
