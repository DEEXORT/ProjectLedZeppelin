package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.dto.EventTo;
import com.quest.entity.Event;
import com.quest.mapping.Dto;
import com.quest.repository.RepositoryImpl;

import java.util.Collection;

public class HibernateEventService implements BaseService<EventTo> {
    private RepositoryImpl<Event> repository;
    private Dto dto = Dto.MAPPER;

    public HibernateEventService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, Event.class);
    }


    public EventTo get(long id) {
        return dto.from(repository.get(id));
    }

    public Collection<EventTo> getAll() {
        return repository.getAll().stream().map(dto::from).toList();
    }

    public void create(EventTo eventTo) {
        Event eventEntity = dto.from(eventTo);
        repository.create(eventEntity);
        eventTo.setId(eventEntity.getId());
    }

    public void update(EventTo eventTo) {
        Event eventEntity = dto.from(eventTo);
        repository.update(eventEntity);
        eventTo.setId(eventEntity.getId());
    }

    public void delete(EventTo eventTo) {
        repository.delete(dto.from(eventTo));
    }
}
