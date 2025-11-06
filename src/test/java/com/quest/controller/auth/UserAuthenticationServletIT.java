package com.quest.controller.auth;

import com.quest.ConfigIT;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class UserAuthenticationServletIT extends ConfigIT {

    private static UserAuthenticationServlet userAuthenticationServlet;

    @BeforeAll
    static void setUp() {
        userAuthenticationServlet = new UserAuthenticationServlet();
        userAuthenticationServlet.init(servletConfig);
    }

    @Test
    void doGet_ShouldRedirectToProfile_WhenUserIsAuthenticated() throws IOException, ServletException {
        // given
        when(session.getAttribute(KeyAttribute.USER)).thenReturn(userTest);

        // when
        userAuthenticationServlet.doGet(request, response);

        // then
        verify(response).sendRedirect(Route.PROFILE);
    }

    @Test
    void doGet_ShouldForwardToLogin_WhenUserIsNotAuthenticated() throws IOException, ServletException {
        // given
        userAuthenticationServlet.init(servletConfig);
        when(session.getAttribute(KeyAttribute.USER)).thenReturn(null);
        when(request.getRequestDispatcher(JspPath.LOGIN)).thenReturn(requestDispatcher);

        // when
        userAuthenticationServlet.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doPost_ShouldRedirectToGame_WhenUserIsAuthenticated() throws IOException, ServletException {
        // given
        userAuthenticationServlet.init(servletConfig);
        when(request.getParameter(KeyAttribute.PASSWORD)).thenReturn("admin");
        when(request.getParameter(KeyAttribute.USERNAME)).thenReturn("admin");

        // when
        userAuthenticationServlet.doPost(request, response);

        // then
        verify(response).sendRedirect(Route.GAME);
    }

    @Test
    void doPost_ShouldRedirectToLogin_WhenUserIsNotAuthenticated() throws IOException, ServletException {
        // given
        userAuthenticationServlet.init(servletConfig);
        when(request.getParameter(KeyAttribute.PASSWORD)).thenReturn("");
        when(request.getParameter(KeyAttribute.USERNAME)).thenReturn("notfoundusername");
        when(request.getRequestDispatcher(JspPath.LOGIN)).thenReturn(requestDispatcher);

        // when
        userAuthenticationServlet.doPost(request, response);

        // then
        verify(response).sendRedirect(request.getRequestURI());
    }

}