package com.quest.services.resolver;

import com.quest.entity.Action;
import com.quest.entity.Event;
import com.quest.entity.Player;
import com.quest.services.EventService;
import com.quest.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
public class EventResolver {
    private EventService eventService;

    public void resolve(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Player player = (Player) session.getAttribute(KeyAttribute.PLAYER);

        long eventId = Long.parseLong(req.getParameter(KeyAttribute.EVENT));
        long nextQuestSceneId = Long.parseLong(req.getParameter(KeyAttribute.SCENE_ID));
        Event event = eventService.get(eventId);

        switch (event.getType()) {
            case DAMAGE -> {
                player.setHealth(player.getHealth() - event.getValue());
            }
            case BUFF -> {
                if (!event.getStat().isEmpty() && event.getStat().equals(EventAttribute.ATTACK)) {
                    player.setAttack(player.getAttack() + event.getValue());
                }
            }
            case HEAL -> {
                player.setHealth(Math.min(player.getHealth() + event.getValue(), player.getMaxHealth()));
            }
            case DEBUFF -> {
                if (!event.getStat().isEmpty() && event.getStat().equals(EventAttribute.ATTACK)) {
                    player.setAttack(player.getAttack() - event.getValue());
                }
            }
        }
        setEventAttributes(event, session, nextQuestSceneId);
        req.getRequestDispatcher(JspPath.QUEST).forward(req, resp);
    }

    private static void setEventAttributes(Event event, HttpSession session, long nextQuestSceneId) {
        String questDescription = event.getDescription().formatted(event.getValue());
        session.setAttribute(KeyAttribute.QUEST_DESCRIPTION, questDescription);
        // Перезапись атрибута QUEST_ACTIONS (только кнопка "Дальше")
        Collection<Action> actions = new ArrayList<>();
        actions.add(Action.builder()
                .actionText(ResourceBundle.getMessage("quest.next"))
                .nextQuestSceneId(nextQuestSceneId)
                .build());
        session.setAttribute(KeyAttribute.QUEST_ACTIONS, actions);
        // Delete Event attribute
        session.removeAttribute(KeyAttribute.EVENT);
    }

}
