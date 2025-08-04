package com.quest.controller.auth;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.quest.util.Const.PATH_PROFILE_JSP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProfileIT extends ConfigIT {
    private final Profile profile = ServiceLocator.getService(Profile.class);

    @Test
    void doGet_ShouldForwardToProfileJsp() throws ServletException, IOException {
        // given
        when(request.getRequestDispatcher(PATH_PROFILE_JSP)).thenReturn(requestDispatcher);

        // when
        profile.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }
}