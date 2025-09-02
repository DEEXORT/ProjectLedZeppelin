package com.quest.controller;

import com.quest.config.ConfigApplication;
import com.quest.config.ServiceLocator;
import com.quest.util.JspPath;
import com.quest.util.Route;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(Route.INDEX)
public class IndexServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        ConfigApplication configApplication = ServiceLocator.getService(ConfigApplication.class);
        configApplication.initApplication();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspPath.INDEX).forward(req, resp);
    }
}
