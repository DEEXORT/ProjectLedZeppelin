package com.quest.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.quest.util.Const.ROUTE_LOGIN;
import static com.quest.util.Const.ROUTE_LOGOUT;

@WebServlet(ROUTE_LOGOUT)
public class Logout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        resp.sendRedirect(ROUTE_LOGIN);
    }
}
