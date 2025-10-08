package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.Event;
import com.quest.repository.Repository;
import com.quest.repository.RepositoryImpl;

public class HibernateEventService extends AbstractBaseService<Event> {

    public HibernateEventService() {
        super(ServiceLocator.getService(RepositoryImpl.class, Event.class));
    }

    public HibernateEventService(Repository<Event> repository) {
        super(repository);
    }
}
