package com.quest.controller;

import com.quest.config.ServiceLocator;
import com.quest.services.StatisticService;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(Route.LEADER_BOARD)
public class LeaderBoardServlet extends HttpServlet {
    private final StatisticService statisticService;

    public LeaderBoardServlet(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    public LeaderBoardServlet() {
        this(ServiceLocator.getService(StatisticService.class));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(KeyAttribute.STATS, statisticService.getUserStats());
        req.getRequestDispatcher(JspPath.LEADER_BOARD).forward(req, resp);
    }
}
