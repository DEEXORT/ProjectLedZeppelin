package com.quest.entity.character;

import com.quest.entity.Ability;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
@SuperBuilder(toBuilder = true)
public abstract class Character {
    Long id;
    String name;
    int level;
    int health;
    int maxHealth;
    int attack;
    @Builder.Default
    Collection<Ability> abilities = new ArrayList<>();
    @Builder.Default
    Map<Ability, Integer> cooldowns = new HashMap<>();

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

    private void useDefense(Ability ability) {

    }

    private void useHeal(Ability ability, Character character) {

    }

    private void useDamage(Ability ability, Character target) {
        int damage = calculateDamage(ability);
        target.setHealth(target.getHealth() - damage);
    }

    private int calculateDamage(Ability ability) {
        return ability.getValue() + ( this.getAttack() * ability.getLevel() / 10);
    }
}
