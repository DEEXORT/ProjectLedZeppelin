package com.quest.services;

import com.quest.entity.Event;
import com.quest.repository.EventRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventService {
    private EventRepository eventRepository;

    public void create(Event event) {
        eventRepository.create(event);
    }

    public Event get(long eventId) {
        return eventRepository.get(eventId);
    }
}
