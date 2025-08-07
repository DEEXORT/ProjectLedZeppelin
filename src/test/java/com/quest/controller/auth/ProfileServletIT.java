package com.quest.controller.auth;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.util.JspPath;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProfileServletIT extends ConfigIT {
    private final ProfileServlet profileServlet = ServiceLocator.getService(ProfileServlet.class);

    @Test
    void doGet_ShouldForwardToProfileJsp() throws ServletException, IOException {
        // given
        when(request.getRequestDispatcher(JspPath.PROFILE)).thenReturn(requestDispatcher);

        // when
        profileServlet.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }
}