package com.quest.services.resolver;

import com.quest.dto.ActionTo;
import com.quest.dto.EventTo;
import com.quest.dto.MonsterTo;
import com.quest.dto.PlayerTo;
import com.quest.entity.Event;
import com.quest.services.hibernate.HibernateEventService;
import com.quest.services.hibernate.HibernateMonsterService;
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
    private HibernateEventService eventService;
    private HibernateMonsterService monsterService;

    public void resolve(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        PlayerTo player = RequestHelper.getValueAttr(req, KeyAttribute.PLAYER, PlayerTo.class);

        long eventId = Long.parseLong(req.getParameter(KeyAttribute.EVENT_ID));
        long nextQuestSceneId = Long.parseLong(req.getParameter(KeyAttribute.SCENE_ID));

        Optional<EventTo> optEvent = eventService.get(eventId);
        if (optEvent.isPresent()) {
            EventTo event = optEvent.get();

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
                    Optional<MonsterTo> optionalMonster = monsterService.get(event.getMonsterId());
                    if (optionalMonster.isPresent()) {
                        MonsterTo monster = optionalMonster.get();
                        monster.setHealth(monster.getMaxHealth());
                        session.setAttribute(KeyAttribute.MONSTER, monster);
                        setEventAttributes(event, session, nextQuestSceneId);
                        resp.sendRedirect(Route.BATTLE);
                    }
                    return;
                }
            }
            req.getRequestDispatcher(JspPath.QUEST).forward(req, resp);
        } else {
            throw new ServletException("Event not found");
        }
    }

    private static void setEventAttributes(EventTo event, HttpSession session, long nextQuestSceneId) {
        String questDescription = event.getDescription().formatted(event.getValue());
        session.setAttribute(KeyAttribute.QUEST_DESCRIPTION, questDescription);
        // Rewrite attribute QUEST_ACTIONS (only button "Next")
        Collection<ActionTo> actions = new ArrayList<>();
        actions.add(ActionTo.builder()
                .actionText(ResourceBundleManager.getMessage("quest.next"))
                .nextQuestSceneId(nextQuestSceneId)
                .build());
        session.setAttribute(KeyAttribute.QUEST_ACTIONS, actions);
    }

}
