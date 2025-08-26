package com.quest.entity.character;

import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.services.AbilityService;
import com.quest.util.ResourceBundleManager;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Data
@SuperBuilder(toBuilder = true)
public abstract class Character {
    public static final Logger logger = LogManager.getLogger(Character.class);
    Long id;
    String name;
    int level;
    int health;
    int maxHealth;
    int attack;
    @Builder.Default
    List<Ability> abilities = initBaseAbilities();
    @Builder.Default
    Map<Ability, Integer> cooldowns = new HashMap<>();

    public static List<Ability> initBaseAbilities() {
        List<Ability> abilities = new ArrayList<>();
        AbilityService abilityService = ServiceLocator.getService(AbilityService.class);
        Ability baseAttack = abilityService.getByName(ResourceBundleManager.getSetting("ability.base_attack_name"));
        abilities.add(baseAttack);
        return abilities;
    }

    public boolean useAbility(Ability ability, Character target) {
        if (cooldowns.getOrDefault(ability, 0) > 0) {
            return false; // unavailable ability
        }

        switch (ability.getType()) {
            case DAMAGE -> this.useDamage(ability, target);
            case HEAL -> this.useHeal(ability, this);
            case DEFENSE -> this.useDefense(ability);
            default -> {
                return false;
            }
        }
        cooldowns.put(ability, ability.getCooldown());
        return true;
    }

    public void updateCooldown(Ability ability) {
        if (cooldowns.getOrDefault(ability, 0) == 0) {
            return;
        }
        cooldowns.put(ability, ability.getCooldown() - 1);
    }

    private void useDefense(Ability ability) {

    }

    private void useHeal(Ability ability, Character character) {

    }

    private void useDamage(Ability ability, Character target) {
        int damage = calculateDamage(ability);
        logger.debug("{} damage", damage);
        target.setHealth(target.getHealth() - damage);
    }

    private int calculateDamage(Ability ability) {
        return ability.getValue() + ( this.getAttack() * ability.getLevel() / 10);
    }

    public Ability getBaseAttack() {
        logger.debug("Size abilities: {}", abilities.size());
        return abilities.get(0);
    }
}
