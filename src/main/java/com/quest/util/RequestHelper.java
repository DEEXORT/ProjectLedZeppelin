package com.quest.util;

import com.quest.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class RequestHelper {

    public void createAuthorizationError(HttpServletRequest req, HttpServletResponse resp, String messageError) throws IOException {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        req.getSession().setAttribute(KeyAttribute.ERROR, messageError);
        resp.sendRedirect(req.getRequestURI());
    }


}
