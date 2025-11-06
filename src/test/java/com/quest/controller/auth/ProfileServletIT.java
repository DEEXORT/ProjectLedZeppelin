package com.quest.controller.auth;

import com.quest.ConfigIT;
import com.quest.dto.AbilityTo;
import com.quest.entity.AbilityType;
import com.quest.util.JspPath;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServletIT extends ConfigIT {

    private static ProfileServlet profileServlet;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        profileServlet = new ProfileServlet();
        profileServlet.init(servletConfig);
    }

    @Test
    void doGet_ShouldForwardToProfileJsp() throws ServletException, IOException {
        // given
        Collection<AbilityTo> abilities = List.of(AbilityTo.builder()
                .id(1L)
                .cooldown(2)
                .description("Test ability")
                .level(1)
                .type(AbilityType.DAMAGE)
                .levelRequirement(3)
                .build());
        when(request.getRequestDispatcher(JspPath.PROFILE)).thenReturn(requestDispatcher);

        // when
        profileServlet.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }
}