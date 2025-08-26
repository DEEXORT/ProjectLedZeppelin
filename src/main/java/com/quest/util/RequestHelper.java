package com.quest.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@UtilityClass
public class RequestHelper {
    public static final Logger logger = LogManager.getLogger(RequestHelper.class);

    public void createAuthorizationError(HttpServletRequest req, HttpServletResponse resp, String messageError) throws IOException {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        req.getSession().setAttribute(KeyAttribute.ERROR, messageError);
        resp.sendRedirect(req.getRequestURI());
    }

    public <T> T getValueAttr(HttpServletRequest req, String key, Class<T> clazz) {
        Object value = req.getSession().getAttribute(key);
        if (value == null) {
            String errorMessage = "No value found for key: " + key;
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage);
        } else {
            return clazz.cast(value);
        }
    }
}
