package com.quest.services.resolver;

import com.quest.dto.QuestSceneTo;
import com.quest.entity.QuestSceneType;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.Route;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@AllArgsConstructor
public class QuestResolver {
    private final Logger logger = LogManager.getLogger(QuestResolver.class);

    public void resolve(HttpServletRequest req, HttpServletResponse resp, QuestSceneTo questScene) throws IOException, ServletException {
        // Если сцена - концовка
        if (questScene.getType() == QuestSceneType.COMPLETE) {
            resp.sendRedirect(Route.END);
            return;
        }

        // Запись сцены в атрибуты для отображения в JSP.
        HttpSession session = req.getSession();
        session.setAttribute(KeyAttribute.BATTLE_FLAG, false);
        setQuestSceneToSessionAttributes(session, questScene);
        req.getRequestDispatcher(JspPath.QUEST).forward(req, resp);
    }

    private void setQuestSceneToSessionAttributes(HttpSession session, QuestSceneTo questScene) {
        questScene.getActions().forEach(action -> logger.debug("QuestScene found. Actions: {}", action));
        session.setAttribute(KeyAttribute.QUEST_DESCRIPTION, questScene.getDescriptionScene());
        session.setAttribute(KeyAttribute.QUEST_ACTIONS, questScene.getActions());
    }
}
