package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.dto.MonsterTo;
import com.quest.entity.character.Monster;
import com.quest.entity.character.MonsterType;
import com.quest.entity.factory.MonsterFactory;
import com.quest.mapping.Dto;
import com.quest.repository.Repository;
import com.quest.repository.RepositoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class HibernateMonsterService implements BaseService<MonsterTo> {
    private Repository<Monster> repository;
    private Dto dto = Dto.MAPPER;

    public HibernateMonsterService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, Monster.class);
    }

    public MonsterTo get(long id) {
        return dto.from(repository.get(id));
    }

    public Collection<MonsterTo> getAll() {
        return repository.getAll().stream().map(dto::from).collect(Collectors.toList());
    }

    public void create(MonsterTo monsterTo) {
        Monster monsterEntity = dto.from(monsterTo);
        repository.create(monsterEntity);
        monsterTo.setId(monsterEntity.getId());
    }

    public void update(MonsterTo monsterTo) {
        repository.update(dto.from(monsterTo));
    }

    public MonsterTo getRandomMonster() {
        Monster patternMonster = Monster.builder().type(MonsterType.COMMON).build();
        Stream<MonsterTo> monsterStream = repository.find(patternMonster).map(dto::from);
        List<MonsterTo> monsters = monsterStream.collect(Collectors.toList());
        Collections.shuffle(monsters, ThreadLocalRandom.current());

        if (monsters.isEmpty()) {
            log.error("No monsters found");
            throw new NoSuchElementException("No monsters found in repository");
        }
        MonsterTo monster = monsters.get(0);
        monster.setHealth(monster.getMaxHealth()); // in repository may be monster with health = 0
        return monster;
    }

    public Optional<MonsterTo> get(MonsterTo monster) {
        return repository.find(dto.from(monster)).map(dto::from).findFirst();
    }

    public void fillMonsterRepository() {
        MonsterTo goblin = MonsterFactory.createGoblinMonster();
        MonsterTo orc = MonsterFactory.createOrcMonster();
        repository.create(dto.from(goblin));
        repository.create(dto.from(orc));
    }

    public void delete(MonsterTo monsterTo) {
        repository.delete(dto.from(monsterTo));
    }
}
