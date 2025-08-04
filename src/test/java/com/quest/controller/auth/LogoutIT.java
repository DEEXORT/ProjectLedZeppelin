package com.quest.controller.auth;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.quest.util.Const.ROUTE_LOGIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class LogoutIT extends ConfigIT {
    private final Logout logout = ServiceLocator.getService(Logout.class);

    @Test
    void doPost_ShouldInvalidateAndRedirectToLogin() throws ServletException, IOException {
        // given + when
        logout.doPost(request, response);

        // then
        verify(session).invalidate();
        verify(response).sendRedirect(ROUTE_LOGIN);
    }
}