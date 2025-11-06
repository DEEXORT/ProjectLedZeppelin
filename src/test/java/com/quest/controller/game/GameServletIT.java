package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.dto.PlayerTo;
import com.quest.dto.UserTo;
import com.quest.services.hibernate.HibernateUserService;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.quest.util.KeyAttribute.PLAYER;
import static com.quest.util.KeyAttribute.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameServletIT extends ConfigIT {
    private static GameServlet gameServlet;
    private static HibernateUserService hibernateUserService;

    @BeforeAll
    static void setUp() throws ServletException {
        gameServlet = ServiceLocator.getService(GameServlet.class);
        gameServlet.init(servletConfig);
        hibernateUserService = ServiceLocator.getService(HibernateUserService.class);
    }

    @Test
    void doGet_ShouldCreateNewGame() throws ServletException, IOException {
        // given
        UserTo testUserGameController = UserTo.builder()
                .login("testUserGameController")
                .password("testUserGameController")
                .build();
        hibernateUserService.create(testUserGameController);
        when(session.getAttribute(USER)).thenReturn(testUserGameController);

        // when
        gameServlet.doGet(request, response);

        // then
        verify(session).setAttribute(eq(PLAYER), any(PlayerTo.class));
        verify(response).sendRedirect(Route.QUEST);
    }

    @Test
    void doGet_ShouldContinueGame() throws ServletException, IOException {

    }
}