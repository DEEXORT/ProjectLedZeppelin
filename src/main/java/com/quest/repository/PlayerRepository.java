package com.quest.repository;

import com.quest.entity.character.Player;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PlayerRepository {
    private static AtomicLong id = new AtomicLong();
    private Map<Long, Player> repository = new ConcurrentHashMap<>();

    public Collection<Player> getAll() {
        return repository.values();
    }

    public Player get(long id) {
        return repository.get(id);
    }

    public void create(Player player) {
        player.setId(id.incrementAndGet());
        update(player);
    }

    public void update(Player player) {
        repository.put(player.getId(), player);
    }

    public void delete(long id) {
        repository.remove(id);
    }
}
