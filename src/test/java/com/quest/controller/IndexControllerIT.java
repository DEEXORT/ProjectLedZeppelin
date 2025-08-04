package com.quest.controller;

import com.quest.ConfigIT;
import com.quest.config.ServiceLocator;
import org.junit.jupiter.api.Test;

import static com.quest.util.Const.PATH_INDEX_JSP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IndexControllerIT extends ConfigIT {
    private final IndexController controller = ServiceLocator.getService(IndexController.class);

    @Test
    void doGet_ShouldShowStartPage() throws Exception {
        // given
        when(request.getRequestDispatcher(PATH_INDEX_JSP)).thenReturn(requestDispatcher);

        // when
        controller.doGet(request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

}