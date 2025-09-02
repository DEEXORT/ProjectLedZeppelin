package com.quest.services;

import com.quest.entity.character.Player;
import com.quest.repository.PlayerRepository;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
public class PlayerService {
    private PlayerRepository playerRepository;

    public Optional<Player> get(long playerId) {
        Player player = playerRepository.get(playerId);
        return player != null ? Optional.of(player) : Optional.empty();
    }

    public Collection<Player> getAll() {
        return playerRepository.getAll();
    }

    public void create(Player player) {
        playerRepository.create(player);
    }

    public void update(Player player) {
        playerRepository.update(player);
    }

    public void delete(Player player) { playerRepository.delete(player.getId()); }

}
