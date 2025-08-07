package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.entity.Player;
import com.quest.entity.User;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

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
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(testPlayerEndGame);
        when(session.getAttribute(KeyAttribute.USER)).thenReturn(testUserEndGame);
        when(request.getRequestDispatcher(JspPath.END_GAME)).thenReturn(requestDispatcher);

        // when
        endGame.doGet(request, response);

        // then
        assertNull(((User) request.getSession().getAttribute(KeyAttribute.USER)).getPlayerId());
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
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(testPlayerEndGame);
        when(session.getAttribute(KeyAttribute.USER)).thenReturn(testUserEndGame);
        when(request.getRequestDispatcher(JspPath.END_GAME)).thenReturn(requestDispatcher);

        // when
        endGame.doGet(request, response);

        // then
        assertNull(((User) request.getSession().getAttribute(KeyAttribute.USER)).getPlayerId());
        verify(requestDispatcher).forward(request, response);
    }
}