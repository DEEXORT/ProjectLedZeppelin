package com.quest.entity.factory;

import com.quest.dto.MonsterTo;
import com.quest.entity.character.MonsterType;

public class MonsterFactory {

    public static MonsterTo createMonster(String name, int level, int maxHealth, int attack, MonsterType type) {
        MonsterTo monster = MonsterTo.builder()
                .name(name)
                .type(type)
                .level(level)
                .health(maxHealth)
                .maxHealth(maxHealth)
                .attack(attack)
                .build();
        monster.initBaseAbilities();
        return monster;
    }

    public static MonsterTo createGoblinMonster() {
        return createMonster("Goblin", 1, 50, 10, MonsterType.COMMON);
    }

    public static MonsterTo createOrcMonster() {
        return createMonster("Orc", 2, 300, 20, MonsterType.ELITE);
    }
}
