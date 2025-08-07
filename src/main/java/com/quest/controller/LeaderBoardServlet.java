package com.quest.controller;

import com.quest.config.ServiceLocator;
import com.quest.services.PlayerService;
import com.quest.services.QuestService;
import com.quest.services.StatisticService;
import com.quest.services.UserService;
import com.quest.util.*;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(Route.LEADER_BOARD)
public class LeaderBoardServlet extends HttpServlet {
    private PlayerService playerService;
    private QuestService questService;
    private UserService userService;
    private StatisticService statisticService;

    @Override
    public void init(ServletConfig config) {
        statisticService = ServiceLocator.getService(StatisticService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(KeyAttribute.STATS, statisticService.getUserStats());
        req.getRequestDispatcher(JspPath.LEADER_BOARD).forward(req, resp);
    }
}
