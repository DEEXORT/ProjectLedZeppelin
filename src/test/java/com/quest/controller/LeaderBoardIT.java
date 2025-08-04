package com.quest.controller;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.entity.Achievement;
import com.quest.entity.Player;
import com.quest.entity.User;
import com.quest.entity.UserStat;
import com.quest.services.PlayerService;
import com.quest.util.StatusPlayer;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.quest.util.Const.PATH_LEADER_BOARD_JSP;
import static com.quest.util.KeyAttribute.STATS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LeaderBoardIT extends ConfigIT {
    private final LeaderBoard leaderBoard = ServiceLocator.getService(LeaderBoard.class);

    @Test
    void doGet_ShouldShowLeaderBoard() throws ServletException, IOException {
        // given
        leaderBoard.init(servletConfig);
        when(request.getRequestDispatcher(PATH_LEADER_BOARD_JSP)).thenReturn(requestDispatcher);

        // when
        leaderBoard.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request,response);
    }
}