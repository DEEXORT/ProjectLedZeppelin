package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.character.Player;
import com.quest.repository.RepositoryImpl;

public class HibernatePlayerService extends AbstractBaseService<Player> {

    public HibernatePlayerService() {
        super(ServiceLocator.getService(RepositoryImpl.class, Player.class));
    }

    public HibernatePlayerService(RepositoryImpl<Player> repository) {
        super(repository);
    }
}
