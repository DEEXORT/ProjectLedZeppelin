package com.quest.services.resolver;

import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.entity.BattleHistory;
import com.quest.entity.character.Character;
import com.quest.entity.character.Monster;
import com.quest.entity.character.Player;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.util.ResourceBundleManager;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Data
public class BattleResolver {
    private static final Logger logger = LogManager.getLogger(BattleResolver.class);
    private final HibernatePlayerService playerService = ServiceLocator.getService(HibernatePlayerService.class);

    public void resolveBattle(Player player, Monster monster, Ability ability, BattleHistory history) {
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

    public void attack(Character attacker, Character target, Ability attackAbility, BattleHistory history) {
        logger.debug("{} attacking {} with ability {}", attacker.getName(), target.getName(), attackAbility.getName());
        boolean isUsedAbility = attacker.useAbility(attackAbility, target, history);
        if (isUsedAbility) {
            // Update other abilities
            attacker.getCooldowns().forEach((ability, cooldown) -> {
                if (ability != attackAbility) {
                    attacker.updateCooldown(ability);
                    if (attacker instanceof Player) {
                        playerService.update((Player) attacker);
                    }
                }
            });
        } else {
            logger.error("Ability was not used");
        }
    }

    public void handleMonsterDefeat(Player player, Monster monster) {
        if (monster.getHealth() <= 0) {
            int deltaExperience = Integer.parseInt(ResourceBundleManager.getSetting("player.increase_experience_after_battle"));
            player.increaseExperience(monster.getLevel() + deltaExperience);
            player.resetCooldowns();
        }
    }
}
