package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.character.Monster;
import com.quest.entity.factory.MonsterFactory;
import com.quest.repository.Repository;
import com.quest.repository.RepositoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HibernateMonsterService extends AbstractBaseService<Monster> {
    private static final Logger logger = LogManager.getLogger(HibernateMonsterService.class);

    public HibernateMonsterService() {
        super(ServiceLocator.getService(RepositoryImpl.class, Monster.class));
    }

    public HibernateMonsterService(Repository<Monster> repository) {
        super(repository);
    }

    public Monster getRandomMonster() {
        Monster patternMonster = Monster.builder().type(Monster.MonsterType.COMMON).build();
        Stream<Monster> monsterStream = repository.find(patternMonster);
        List<Monster> monsters = monsterStream.collect(Collectors.toList());
        Collections.shuffle(monsters, ThreadLocalRandom.current());

        if (monsters.isEmpty()) {
            logger.error("No monsters found");
            throw new NoSuchElementException("No monsters found in repository");
        }
        Monster monster = monsters.get(0);
        monster.setHealth(monster.getMaxHealth()); // in repository may be monster with health = 0
        return monster;
    }

    public Optional<Monster> get(Monster monster) {
        return repository.find(monster).findFirst();
    }

    public void fillMonsterRepository() {
        Monster goblin = MonsterFactory.createGoblinMonster();
        Monster orc = MonsterFactory.createOrcMonster();
        repository.create(goblin);
        repository.create(orc);
    }
}
