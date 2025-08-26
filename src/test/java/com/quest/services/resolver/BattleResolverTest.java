package com.quest.services.resolver;

import com.quest.entity.Ability;
import com.quest.entity.character.Monster;
import com.quest.entity.character.Player;
import com.quest.entity.factory.AbilityFactory;
import com.quest.entity.factory.MonsterFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleResolverTest {

    @Test
    void attack() {
        // given
        Player player = Player.builder()
                .name("Player")
                .level(1)
                .health(100)
                .maxHealth(100)
                .attack(50)
                .build();
        Monster monster = MonsterFactory.createGoblinMonster();
        Ability ability = AbilityFactory
                .createDamageAbility("Hadoooken", "Hadoooken", 1, 100, 3);

        // when
        BattleResolver battleResolver = new BattleResolver();
        battleResolver.attack(player, monster, ability);

        // then
        assertTrue(monster.getHealth() < monster.getMaxHealth());
    }
}