package com.quest.entity.factory;

import com.quest.entity.Ability;
import com.quest.entity.character.Monster;

public class MonsterFactory {

    public static Monster createMonster(String name, int level, int maxHealth, int attack) {
        return Monster.builder()
                .name(name)
                .level(level)
                .health(maxHealth)
                .maxHealth(maxHealth)
                .attack(attack)
                .build();
    }

    public static Monster createGoblinMonster() {
        return createMonster("Goblin", 1, 50, 10);
    }

    public static Monster createOrcMonster() {
        return createMonster("Orc", 2, 300, 20);
    }
}
