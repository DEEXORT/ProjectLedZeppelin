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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@AllArgsConstructor
@Slf4j
public class QuestResolver {

    public void resolve(HttpServletRequest req, HttpServletResponse resp, QuestSceneTo questScene) throws IOException, ServletException {
        // If end scene
        if (questScene.getType() == QuestSceneType.COMPLETE) {
            resp.sendRedirect(Route.END);
            return;
        }

        // Writing the scene to session attributes for show through JSP
        HttpSession session = req.getSession();
        session.setAttribute(KeyAttribute.BATTLE_FLAG, false);
        setQuestSceneToSessionAttributes(session, questScene);
        req.getRequestDispatcher(JspPath.QUEST).forward(req, resp);
    }

    private void setQuestSceneToSessionAttributes(HttpSession session, QuestSceneTo questScene) {
        questScene.getActions().forEach(action -> log.debug("QuestScene found. Actions: {}", action));
        session.setAttribute(KeyAttribute.QUEST_DESCRIPTION, questScene.getDescriptionScene());
        session.setAttribute(KeyAttribute.QUEST_ACTIONS, questScene.getActions());
    }
}
