package com.quest.controller.auth;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.quest.util.KeyAttribute.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserRegistrationServletIT extends ConfigIT {
    private final UserRegistrationServlet userRegistrationServlet = ServiceLocator.getService(UserRegistrationServlet.class);

    @Test
    void doGet_ShouldRedirectToProfile_WhenUserIsAuthenticated() throws ServletException, IOException {
        // given
        when(session.getAttribute(KeyAttribute.USER)).thenReturn(userTest);
        when(request.getRequestDispatcher(JspPath.REGISTER)).thenReturn(requestDispatcher);

        // when
        userRegistrationServlet.doGet(request, response);

        // then
        verify(response).sendRedirect(Route.PROFILE);
    }

    @Test
    void doGet_ShouldForwardToRegister_WhenUserIsNotAuthenticated() throws ServletException, IOException {
        // given
        when(session.getAttribute(USER)).thenReturn(null);
        when(request.getRequestDispatcher(JspPath.REGISTER)).thenReturn(requestDispatcher);

        // when
        userRegistrationServlet.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doPost_ShouldRedirectToGame_AfterRegistration() throws ServletException, IOException {
        // given
        userRegistrationServlet.init(servletConfig);
        when(request.getParameter(KeyAttribute.USERNAME)).thenReturn("newUser");
        when(request.getParameter(KeyAttribute.PASSWORD)).thenReturn("newUser");

        // when
        userRegistrationServlet.doPost(request, response);

        // then
        verify(response).sendRedirect(Route.GAME);
    }

    @Test
    void doPost_ShouldRedirectToRegister_WhenInvalidateCredentials() throws ServletException, IOException {
        userRegistrationServlet.init(servletConfig);
        when(request.getParameter(KeyAttribute.USERNAME)).thenReturn("");
        when(request.getParameter(KeyAttribute.PASSWORD)).thenReturn("");
        when(request.getRequestDispatcher(JspPath.REGISTER)).thenReturn(requestDispatcher);

        // when
        userRegistrationServlet.doPost(request, response);

        // then
        verify(response).sendRedirect(request.getRequestURI());
    }
}