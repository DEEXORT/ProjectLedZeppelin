package com.quest.services.resolver;

import com.quest.entity.Action;
import com.quest.entity.Event;
import com.quest.entity.Monster;
import com.quest.entity.Player;
import com.quest.services.EventService;
import com.quest.services.MonsterService;
import com.quest.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
public class EventResolver {
    private EventService eventService;
    private MonsterService monsterService;

    public void resolve(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        Player player = (Player) session.getAttribute(KeyAttribute.PLAYER);

        long eventId = Long.parseLong(req.getParameter(KeyAttribute.EVENT_ID));
        long nextQuestSceneId = Long.parseLong(req.getParameter(KeyAttribute.SCENE_ID));
        Event event = eventService.get(eventId);

        switch (event.getType()) {
            case DAMAGE -> {
                player.setHealth(player.getHealth() - event.getValue());
                setEventAttributes(event, session, nextQuestSceneId);
            }
            case BUFF -> {
                if (!event.getStat().isEmpty() && event.getStat().equals(EventAttribute.ATTACK)) {
                    player.setAttack(player.getAttack() + event.getValue());
                    setEventAttributes(event, session, nextQuestSceneId);
                }
            }
            case HEAL -> {
                player.setHealth(Math.min(player.getHealth() + event.getValue(), player.getMaxHealth()));
                setEventAttributes(event, session, nextQuestSceneId);
            }
            case DEBUFF -> {
                if (!event.getStat().isEmpty() && event.getStat().equals(EventAttribute.ATTACK)) {
                    player.setAttack(player.getAttack() - event.getValue());
                    setEventAttributes(event, session, nextQuestSceneId);
                }
            }
            case BATTLE -> {
                Optional<Monster> optionalMonster = monsterService.get(event.getMonsterId());
                if (optionalMonster.isPresent()) {
                    session.setAttribute(KeyAttribute.MONSTER, optionalMonster.get());
                    setEventAttributes(event, session, nextQuestSceneId);
                    resp.sendRedirect(Route.BATTLE);
                }
                return;
            }
        }
        req.getRequestDispatcher(JspPath.QUEST).forward(req, resp);
    }

    private static void setEventAttributes(Event event, HttpSession session, long nextQuestSceneId) {
        String questDescription = event.getDescription().formatted(event.getValue());
        session.setAttribute(KeyAttribute.QUEST_DESCRIPTION, questDescription);
        // Rewrite attribute QUEST_ACTIONS (only button "Next")
        Collection<Action> actions = new ArrayList<>();
        actions.add(Action.builder()
                .actionText(ResourceBundle.getMessage("quest.next"))
                .nextQuestSceneId(nextQuestSceneId)
                .build());
        session.setAttribute(KeyAttribute.QUEST_ACTIONS, actions);
    }

}
