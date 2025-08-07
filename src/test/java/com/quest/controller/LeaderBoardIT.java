package com.quest.controller;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.util.JspPath;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LeaderBoardIT extends ConfigIT {
    private final LeaderBoard leaderBoard = ServiceLocator.getService(LeaderBoard.class);

    @Test
    void doGet_ShouldShowLeaderBoard() throws ServletException, IOException {
        // given
        leaderBoard.init(servletConfig);
        when(request.getRequestDispatcher(JspPath.LEADER_BOARD)).thenReturn(requestDispatcher);

        // when
        leaderBoard.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request,response);
    }
}