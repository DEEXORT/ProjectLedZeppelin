package com.quest.repository;

import com.quest.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PlayerRepository implements Repository<Player> {
    private static AtomicLong id = new AtomicLong();
    private Map<Long, Player> playerRepository = new ConcurrentHashMap<>();

    @Override
    public Collection<Player> getAll() {
        return playerRepository.values();
    }

    @Override
    public Player get(long id) {
        return playerRepository.get(id);
    }

    @Override
    public void create(Player player) {
        player.setId(id.incrementAndGet());
        update(player);
    }

    @Override
    public void update(Player player) {
        playerRepository.put(player.getId(), player);
    }

    @Override
    public void delete(long id) {
        playerRepository.remove(id);
    }
}
