package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.dto.PlayerTo;
import com.quest.entity.character.Player;
import com.quest.mapping.Dto;
import com.quest.repository.RepositoryImpl;

import java.util.Collection;

public class HibernatePlayerService implements BaseService<PlayerTo> {
    private final RepositoryImpl<Player> repository;
    private final Dto dto = Dto.MAPPER;

    public HibernatePlayerService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, Player.class);
    }

    public PlayerTo get(long id) {
        return dto.from(repository.get(id));
    }

    public Collection<PlayerTo> getAll() {
        return repository.getAll().stream().map(dto::from).toList();
    }

    public void create(PlayerTo playerTo) {
        Player player = dto.from(playerTo);
        repository.create(player);
        playerTo.setId(player.getId());
    }

    public void update(PlayerTo playerTo) {
        repository.update(dto.from(playerTo));
    }

    public void delete(PlayerTo player) {
        repository.delete(dto.from(player));
    }
}
