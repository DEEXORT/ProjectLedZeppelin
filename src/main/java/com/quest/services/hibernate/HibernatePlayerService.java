package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.character.Player;
import com.quest.repository.RepositoryImpl;

import java.util.Collection;
import java.util.Optional;

public class HibernatePlayerService implements BaseService<Player> {
    private final RepositoryImpl<Player> repository;

    public HibernatePlayerService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, Player.class);
    }

    public HibernatePlayerService(RepositoryImpl<Player> repository) {
        this.repository = repository;
    }

    public void create(Player player) {
        repository.create(player);
    }

    public Collection<Player> getAll() {
        return repository.getAll();
    }

    @Override
    public Optional<Player> get(long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public void update(Player entity) {
        repository.update(entity);
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }
}
