package com.quest.controller;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import com.quest.util.JspPath;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IndexServletIT extends ConfigIT {
    private final IndexServlet controller = ServiceLocator.getService(IndexServlet.class);

    @Test
    void doGet_ShouldShowStartPage() throws Exception {
        // given
        when(request.getRequestDispatcher(JspPath.INDEX)).thenReturn(requestDispatcher);

        // when
        controller.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

}