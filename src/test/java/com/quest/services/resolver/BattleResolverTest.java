package com.quest.services.resolver;

import com.quest.dto.AbilityTo;
import com.quest.dto.MonsterTo;
import com.quest.dto.PlayerTo;
import com.quest.entity.Ability;
import com.quest.entity.BattleHistory;
import com.quest.entity.character.Monster;
import com.quest.entity.character.Player;
import com.quest.entity.factory.AbilityFactory;
import com.quest.entity.factory.MonsterFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BattleResolverTest {

    @Test
    void attack() {
        // given
        PlayerTo player = PlayerTo.builder()
                .name("Player")
                .level(1)
                .health(100)
                .maxHealth(100)
                .attack(50)
                .build();
        MonsterTo monster = MonsterFactory.createGoblinMonster();
        AbilityTo ability = AbilityFactory
                .createDamageAbility("Hadoooken", "Hadoooken", 1, 100, 3);
        BattleHistory history = new BattleHistory();
        BattleResolver battleResolver = new BattleResolver();

        // when
        battleResolver.attack(player, monster, ability, history);

        // then
        assertTrue(monster.getHealth() < monster.getMaxHealth());
    }
}