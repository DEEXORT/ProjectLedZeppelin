package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.entity.User;
import com.quest.entity.character.Player;
import com.quest.services.hibernate.HibernateUserService;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.quest.util.KeyAttribute.PLAYER;
import static com.quest.util.KeyAttribute.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameServletIT extends ConfigIT {
    private final GameServlet gameServlet = ServiceLocator.getService(GameServlet.class);
    private HibernateUserService hibernateUserService;

    @BeforeEach
    void setUp() {
        hibernateUserService = ServiceLocator.getService(HibernateUserService.class);
    }

    @Test
    void doGet_ShouldCreateNewGame() throws ServletException, IOException {
        // given
        User testUserGameController = User.builder()
                .login("testUserGameController")
                .password("testUserGameController")
                .build();
        hibernateUserService.create(testUserGameController);
        when(session.getAttribute(USER)).thenReturn(testUserGameController);

        // when
        gameServlet.doGet(request, response);

        // then
        verify(session).setAttribute(eq(PLAYER), any(Player.class));
        verify(response).sendRedirect(Route.QUEST);
    }

    @Test
    void doGet_ShouldContinueGame() throws ServletException, IOException {

    }
}