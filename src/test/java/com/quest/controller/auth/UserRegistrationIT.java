package com.quest.controller.auth;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.quest.util.Const.*;
import static com.quest.util.KeyAttribute.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserRegistrationIT extends ConfigIT {
    private final UserRegistration userRegistration = ServiceLocator.getService(UserRegistration.class);

    @Test
    void doGet_ShouldRedirectToProfile_WhenUserIsAuthenticated() throws ServletException, IOException {
        // given
        when(session.getAttribute(USER)).thenReturn(userTest);
        when(request.getRequestDispatcher(PATH_REGISTER_JSP)).thenReturn(requestDispatcher);

        // when
        userRegistration.doGet(request, response);

        // then
        verify(response).sendRedirect(ROUTE_PROFILE);
    }

    @Test
    void doGet_ShouldForwardToRegister_WhenUserIsNotAuthenticated() throws ServletException, IOException {
        // given
        when(session.getAttribute(USER)).thenReturn(null);
        when(request.getRequestDispatcher(PATH_REGISTER_JSP)).thenReturn(requestDispatcher);

        // when
        userRegistration.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doPost_ShouldRedirectToGame_AfterRegistration() throws ServletException, IOException {
        // given
        userRegistration.init(servletConfig);
        when(request.getParameter(USERNAME)).thenReturn("newUser");
        when(request.getParameter(PASSWORD)).thenReturn("newUser");

        // when
        userRegistration.doPost(request, response);

        // then
        verify(response).sendRedirect(ROUTE_GAME);
    }

    @Test
    void doPost_ShouldRedirectToRegister_WhenInvalidateCredentials() throws ServletException, IOException {
        userRegistration.init(servletConfig);
        when(request.getParameter(USERNAME)).thenReturn("");
        when(request.getParameter(PASSWORD)).thenReturn("");
        when(request.getRequestDispatcher(PATH_REGISTER_JSP)).thenReturn(requestDispatcher);

        // when
        userRegistration.doPost(request, response);

        // then
        verify(response).sendRedirect(ROUTE_REGISTER);
    }
}