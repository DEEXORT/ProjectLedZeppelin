package com.quest.controller.auth;

import com.quest.ConfigIT;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.verify;

class LogoutServletIT extends ConfigIT {
    private LogoutServlet logoutServlet;

    @BeforeEach
    void setUp() {
        logoutServlet = new LogoutServlet();
    }

    @Test
    void doPost_ShouldInvalidateAndRedirectToLogin() throws ServletException, IOException {
        // given + when
        logoutServlet.doPost(request, response);

        // then
        verify(session).invalidate();
        verify(response).sendRedirect(Route.LOGIN);
    }
}