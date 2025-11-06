package com.quest.entity.factory;

import com.quest.dto.MonsterTo;
import com.quest.entity.character.MonsterType;
import com.quest.util.ResourceBundleManager;
import lombok.experimental.UtilityClass;

@UtilityClass
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
        return createMonster(ResourceBundleManager.getSetting("monster.goblin.name"),
                Integer.parseInt(ResourceBundleManager.getSetting("monster.goblin.level")),
                Integer.parseInt(ResourceBundleManager.getSetting("monster.goblin.max_health")),
                Integer.parseInt(ResourceBundleManager.getSetting("monster.goblin.attack")), MonsterType.COMMON);
    }

    public static MonsterTo createOrcMonster() {
        return createMonster(ResourceBundleManager.getSetting("monster.orc.name"),
                Integer.parseInt(ResourceBundleManager.getSetting("monster.orc.level")),
                Integer.parseInt(ResourceBundleManager.getSetting("monster.orc.max_health")),
                Integer.parseInt(ResourceBundleManager.getSetting("monster.orc.attack")), MonsterType.ELITE);
    }
}
