package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.entity.Achievement;
import com.quest.entity.Player;
import com.quest.entity.User;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.quest.util.Const.PATH_END_GAME_JSP;
import static com.quest.util.KeyAttribute.PLAYER;
import static com.quest.util.KeyAttribute.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EndGameIT extends ConfigIT {
    private final EndGame endGame = ServiceLocator.getService(EndGame.class);

    @Test
    void doGet_ShouldEndGame_WhenGameIsOver() throws ServletException, IOException {
        // given
        endGame.init(servletConfig);
        Player testPlayerEndGame = Player.builder()
                .id(35L)
                .name("testPlayerEndGame")
                .level(35)
                .health(70)
                .maxHealth(100)
                .questSceneId(907L)
                .build();
        User testUserEndGame = User.builder()
                .login("testUserEndGame")
                .password("testPassword")
                .id(35L)
                .achievements(new ArrayList<>())
                .playerId(35L)
                .build();
        when(session.getAttribute(PLAYER)).thenReturn(testPlayerEndGame);
        when(session.getAttribute(USER)).thenReturn(testUserEndGame);
        when(request.getRequestDispatcher(PATH_END_GAME_JSP)).thenReturn(requestDispatcher);

        // when
        endGame.doGet(request, response);

        // then
        assertNull(((User) request.getSession().getAttribute(USER)).getPlayerId());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doGet_ShouldEndGame_WhenPlayerIsDeath() throws ServletException, IOException {
        // given
        endGame.init(servletConfig);
        Player testPlayerEndGame = Player.builder()
                .id(35L)
                .userId(35L)
                .name("testPlayerEndGame")
                .level(35)
                .health(70)
                .maxHealth(100)
                .questSceneId(951L)
                .build();
        User testUserEndGame = User.builder()
                .login("testUserEndGame")
                .password("testPassword")
                .id(35L)
                .achievements(new ArrayList<>())
                .playerId(35L)
                .build();
        when(session.getAttribute(PLAYER)).thenReturn(testPlayerEndGame);
        when(session.getAttribute(USER)).thenReturn(testUserEndGame);
        when(request.getRequestDispatcher(PATH_END_GAME_JSP)).thenReturn(requestDispatcher);

        // when
        endGame.doGet(request, response);

        // then
        assertNull(((User) request.getSession().getAttribute(USER)).getPlayerId());
        verify(requestDispatcher).forward(request, response);
    }
}