package com.quest.controller;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.util.JspPath;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LeaderBoardServletIT extends ConfigIT {
    private final LeaderBoardServlet leaderBoardServlet = ServiceLocator.getService(LeaderBoardServlet.class);


    @Test
    void doGet_ShouldShowLeaderBoard() throws ServletException, IOException {
        // given
        leaderBoardServlet.init(servletConfig);
        when(request.getRequestDispatcher(JspPath.LEADER_BOARD)).thenReturn(requestDispatcher);

        // when
        leaderBoardServlet.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }
}