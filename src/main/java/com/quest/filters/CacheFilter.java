package com.quest.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter({"*.scss", "*.png", "*.jpg", "*.woff2"})
public class CacheFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Cache-Control", "public, max-age=86400"); // One day
        response.setDateHeader("Expires", System.currentTimeMillis() + 86400000L);

        System.out.println(((HttpServletRequest) req).getRequestURI());
        chain.doFilter(req, res);
    }
}
