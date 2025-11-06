package com.quest.controller.game;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.dto.UserTo;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EndGameServletIT extends ConfigIT {
    private final EndGameServlet endGameServlet = ServiceLocator.getService(EndGameServlet.class);

    @Test
    void doGet_ShouldEndGame_WhenGameIsOver() throws ServletException, IOException {
        // given
        endGameServlet.init(servletConfig);
        playerTest.setQuestSceneId(907L);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(session.getAttribute(KeyAttribute.USER)).thenReturn(userTest);
        when(request.getRequestDispatcher(JspPath.END_GAME)).thenReturn(requestDispatcher);

        // when
        endGameServlet.doGet(request, response);

        // then
        assertNull(((UserTo) request.getSession().getAttribute(KeyAttribute.USER)).getCharacterId());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doGet_ShouldEndGame_WhenPlayerIsDeath() throws ServletException, IOException {
        // given
        endGameServlet.init(servletConfig);
        playerTest.setQuestSceneId(951L);
        when(session.getAttribute(KeyAttribute.PLAYER)).thenReturn(playerTest);
        when(session.getAttribute(KeyAttribute.USER)).thenReturn(userTest);
        when(request.getRequestDispatcher(JspPath.END_GAME)).thenReturn(requestDispatcher);

        // when
        endGameServlet.doGet(request, response);

        // then
        assertNull(((UserTo) request.getSession().getAttribute(KeyAttribute.USER)).getCharacterId());
        verify(requestDispatcher).forward(request, response);
    }
}