package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.dto.PlayerTo;
import com.quest.entity.character.Player;
import com.quest.mapping.Dto;
import com.quest.repository.RepositoryImpl;

import java.util.Collection;
import java.util.Optional;

public class HibernatePlayerService {
    private final RepositoryImpl<Player> repository;
    private final Dto dto = Dto.MAPPER;

    public HibernatePlayerService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, Player.class);
    }

    public HibernatePlayerService(RepositoryImpl<Player> repository) {
        this.repository = repository;
    }

    public Optional<PlayerTo> get(long id) {
        return Optional.ofNullable(repository.get(id)).map(dto::from);
    }

    public Collection<PlayerTo> getAll() {
        return repository.getAll().stream().map(dto::from).toList();
    }

    public void create(PlayerTo playerTo) {
        repository.create(dto.from(playerTo));
    }

    public void update(PlayerTo playerTo) {
        repository.update(dto.from(playerTo));
    }

    public void delete(PlayerTo player) {
        repository.delete(dto.from(player));
    }
}
