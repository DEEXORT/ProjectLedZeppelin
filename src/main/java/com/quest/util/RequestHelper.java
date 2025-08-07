package com.quest.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestHelper {

    public void createAuthorizationError(HttpServletRequest req, HttpServletResponse resp, String messageError) {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        req.getSession().setAttribute(KeyAttribute.ERROR, messageError);
    }

    public void setAttributeInSession(HttpServletRequest req, String keyAttribute, Object value) {

    }
}
