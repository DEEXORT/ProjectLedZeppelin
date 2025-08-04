package com.quest.controller.auth;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.util.KeyAttribute;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.quest.util.Const.*;
import static com.quest.util.KeyAttribute.PASSWORD;
import static com.quest.util.KeyAttribute.USERNAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserAuthenticationIT extends ConfigIT {
    private final UserAuthentication userAuthentication = ServiceLocator.getService(UserAuthentication.class);

    @Test
    void doGet_ShouldRedirectToProfile_WhenUserIsAuthenticated() throws IOException, ServletException {
        // given
        when(session.getAttribute(KeyAttribute.USER)).thenReturn(userTest);

        // when
        userAuthentication.doGet(request, response);

        // then
        verify(response).sendRedirect(ROUTE_PROFILE);
    }

    @Test
    void doGet_ShouldForwardToLogin_WhenUserIsNotAuthenticated() throws IOException, ServletException {
        // given
        when(session.getAttribute(KeyAttribute.USER)).thenReturn(null);
        when(request.getRequestDispatcher(PATH_LOGIN_JSP)).thenReturn(requestDispatcher);

        // when
        userAuthentication.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doPost_ShouldRedirectToGame_WhenUserIsAuthenticated() throws IOException, ServletException {
        // given
        userAuthentication.init(servletConfig);
        when(request.getParameter(PASSWORD)).thenReturn("admin");
        when(request.getParameter(USERNAME)).thenReturn("admin");

        // when
        userAuthentication.doPost(request, response);

        // then
        verify(response).sendRedirect(ROUTE_GAME);
    }

    @Test
    void doPost_ShouldForwardToLogin_WhenUserIsNotAuthenticated() throws IOException, ServletException {
        // given
        userAuthentication.init(servletConfig);
        when(request.getParameter(PASSWORD)).thenReturn("");
        when(request.getParameter(USERNAME)).thenReturn("notfoundusername");
        when(request.getRequestDispatcher(PATH_LOGIN_JSP)).thenReturn(requestDispatcher);

        // when
        userAuthentication.doPost(request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

}