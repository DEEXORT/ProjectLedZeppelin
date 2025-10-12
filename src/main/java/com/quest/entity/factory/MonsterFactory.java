package com.quest.entity.factory;

import com.quest.entity.character.Monster;

public class MonsterFactory {

    public static Monster createMonster(String name, int level, int maxHealth, int attack, Monster.MonsterType type) {
        Monster monster = Monster.builder()
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

    public static Monster createGoblinMonster() {
        return createMonster("Goblin", 1, 50, 10, Monster.MonsterType.COMMON);
    }

    public static Monster createOrcMonster() {
        return createMonster("Orc", 2, 300, 20, Monster.MonsterType.ELITE);
    }
}
