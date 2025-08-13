package com.quest.services.resolver;

import com.quest.entity.QuestScene;
import com.quest.util.JspPath;
import com.quest.util.KeyAttribute;
import com.quest.util.ParseConst;
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

    public void resolve(HttpServletRequest req, HttpServletResponse resp, QuestScene questScene) throws IOException, ServletException {
        // Если сцена - концовка
        Long questId = questScene.getId();
        if (questId >= ParseConst.ID_END_MIN && questId < ParseConst.ID_END_MAX) {
            resp.sendRedirect(Route.END);
            return;
        }

        // Запись сцены в атрибуты для отображения в JSP.
        HttpSession session = req.getSession();
        session.setAttribute(KeyAttribute.BATTLE_FLAG, false);
        setQuestSceneToSessionAttributes(session, questScene);
        req.getRequestDispatcher(JspPath.QUEST).forward(req, resp);
    }

    private void setQuestSceneToSessionAttributes(HttpSession session, QuestScene questScene) {
        questScene.getActions().forEach(action -> logger.debug("QuestScene found. Actions: {}", action.getActionText()));
        session.setAttribute(KeyAttribute.QUEST_DESCRIPTION, questScene.getDescriptionScene());
        session.setAttribute(KeyAttribute.QUEST_ACTIONS, questScene.getActions());
    }
}
