package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.character.Player;
import com.quest.repository.RepositoryImpl;

import java.util.Collection;

public class HibernatePlayerService {
    private RepositoryImpl<Player> repository;

    public HibernatePlayerService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, Player.class);
    }

    public void create(Player player) {
        repository.create(player);
    }

    public Collection<Player> getAll() {return repository.getAll();}
}
