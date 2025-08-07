package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.entity.Player;
import com.quest.entity.User;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static com.quest.util.KeyAttribute.PLAYER;
import static com.quest.util.KeyAttribute.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameServletIT extends ConfigIT {
    private final GameServlet gameServlet = ServiceLocator.getService(GameServlet.class);

    @Test
    void doGet_ShouldCreateNewGame() throws ServletException, IOException {
        // given
        User testUserGameController = User.builder()
                .id(5L)
                .achievements(new ArrayList<>())
                .login("testUserGameController")
                .password("testUserGameController")
                .build();
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