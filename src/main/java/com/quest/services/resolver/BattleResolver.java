package com.quest.services.resolver;

import com.quest.config.ServiceLocator;
import com.quest.dto.AbilityTo;
import com.quest.dto.CharacterTo;
import com.quest.dto.MonsterTo;
import com.quest.dto.PlayerTo;
import com.quest.entity.BattleHistory;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.util.ResourceBundleManager;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Data
public class BattleResolver {
    private static final Logger logger = LogManager.getLogger(BattleResolver.class);
    private final HibernatePlayerService playerService = ServiceLocator.getService(HibernatePlayerService.class);

    public void resolveBattle(PlayerTo player, MonsterTo monster, AbilityTo ability, BattleHistory history) {
        attack(player, monster, ability, history);

        if (monster.getHealth() > 0) {
            attack(monster, player, monster.getBaseAttack(), history);
            if (player.getHealth() <= 0) {
                player.setHealth(0);
            }
        } else {
            monster.setHealth(0);
        }
    }

    public void attack(CharacterTo attacker, CharacterTo target, AbilityTo attackAbility, BattleHistory history) {
        logger.debug("{} attacking {} with ability {}", attacker.getName(), target.getName(), attackAbility.getName());
        boolean isUsedAbility = attacker.useAbility(attackAbility, target, history);
        if (isUsedAbility) {
            // Update other abilities
            attacker.getCooldowns().forEach((ability, cooldown) -> {
                if (ability != attackAbility) {
                    attacker.updateCooldown(ability);
                    if (attacker instanceof PlayerTo) {
                        playerService.update((PlayerTo) attacker);
                    }
                }
            });
        } else {
            logger.error("Ability was not used");
        }
    }

    public void handleMonsterDefeat(PlayerTo player, MonsterTo monster) {
        if (monster.getHealth() <= 0) {
            int deltaExperience = Integer.parseInt(ResourceBundleManager.getSetting("player.increase_experience_after_battle"));
            player.increaseExperience(monster.getLevel() + deltaExperience);
            player.resetCooldowns();
        }
    }
}
