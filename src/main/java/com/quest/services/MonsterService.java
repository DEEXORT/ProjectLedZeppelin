package com.quest.services;

import com.quest.entity.Monster;
import com.quest.repository.MonsterRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class MonsterService {
    private MonsterRepository monsterRepository;
    private static final Logger logger = LogManager.getLogger(MonsterService.class);

    public void create(Monster monster) {
        monsterRepository.create(monster);
    }

    public void update(Monster monster) {monsterRepository.update(monster);}

    public Optional<Monster> get(long id) {
        Monster monster = monsterRepository.get(id);
        return monster == null ? Optional.empty() : Optional.of(monster);
    }

    public Monster getRandomMonster() {
        Collection<Monster> monsterCollection = monsterRepository.getAll();
        if (monsterCollection.isEmpty()) {
            logger.error("No monsters found");
            throw new NoSuchElementException("No monsters found in repository");
        }
        Monster[] monsters = monsterCollection.toArray(new Monster[0]);
        Monster monster = monsters[ThreadLocalRandom.current().nextInt(monsterCollection.size())];
        monster.setHealth(monster.getMaxHealth()); // in repository may be monster with health = 0
        return monster;
    }
}
