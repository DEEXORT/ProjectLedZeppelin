package com.quest.services.resolver;

import com.quest.entity.Ability;
import com.quest.entity.character.Character;
import com.quest.entity.character.Monster;
import com.quest.entity.character.Player;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Data
public class BattleResolver {
    private static final Logger logger = LogManager.getLogger(BattleResolver.class);

    public void resolveBattle(Player player, Monster monster, Ability ability) {
        attack(player, monster, ability);

        if (monster.getHealth() > 0) {
            attack(monster, player, monster.getBaseAttack());
            if (player.getHealth() <= 0) {
                player.setHealth(0);
            }
        } else {
            monster.setHealth(0);
        }
    }

    public void attack(Character attacker, Character target, Ability attackAbility) {
        logger.debug("{} attacking {} with ability {}", attacker.getName(), target.getName(), attackAbility.getName());
        boolean isUsedAbility = attacker.useAbility(attackAbility, target);
        if (isUsedAbility) {
            // Update other abilities
            attacker.getCooldowns().forEach((ability, cooldown) -> {
                if (ability != attackAbility) attacker.updateCooldown(ability);
            });
        } else {
            logger.error("Ability was not used");
        }
    }
}
