package com.quest.dto;

import com.quest.config.ServiceLocator;
import com.quest.entity.BattleHistory;
import com.quest.services.hibernate.HibernateAbilityService;
import com.quest.util.ResourceBundleManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Slf4j
public class CharacterTo {
    Long id;
    String name;
    int level;
    int health;
    int maxHealth;
    int attack;
    @Builder.Default
    List<AbilityTo> abilities = new ArrayList<>();
    @Builder.Default
    Map<AbilityTo, Integer> cooldowns = new HashMap<>();

    public void initBaseAbilities() {
        HibernateAbilityService abilityService = ServiceLocator.getService(HibernateAbilityService.class);
        AbilityTo baseAttack = abilityService.getByName(ResourceBundleManager.getSetting("ability.base_attack_name"));
        this.abilities.add(baseAttack);
    }

    public boolean useAbility(AbilityTo ability, CharacterTo target, BattleHistory history) {
        if (cooldowns.getOrDefault(ability, 0) > 0) {
            return false; // unavailable ability
        }

        switch (ability.getType()) {
            case DAMAGE -> this.useDamage(ability, target, history);
            case HEAL -> this.useHeal(ability, this, history);
            case DEFENSE -> this.useDefense(ability);
            default -> {
                return false;
            }
        }
        cooldowns.put(ability, ability.getCooldown());
        return true;
    }

    public void updateCooldown(AbilityTo ability) {
        if (cooldowns.getOrDefault(ability, 0) == 0) {
            return;
        }
        cooldowns.put(ability, ability.getCooldown() - 1);
    }

    private void useDefense(AbilityTo ability) {

    }

    private void useHeal(AbilityTo ability, CharacterTo character, BattleHistory history) {
        character.setHealth(Math.min(character.getHealth() + ability.getValue(), character.getMaxHealth()));
        history.saveAction(this, character, ability, ability.getValue());
    }

    private void useDamage(AbilityTo ability, CharacterTo target, BattleHistory history) {
        int damage = calculateDamage(ability);
        log.debug("{} damage", damage);
        target.setHealth(target.getHealth() - damage);
        history.saveAction(this, target, ability, damage);
    }

    private int calculateDamage(AbilityTo ability) {
        return ability.getValue();
    }

    public AbilityTo getBaseAttack() {
        log.debug("Size abilities: {}", abilities.size());
        return abilities.get(0);
    }

    public boolean isAbilityAvailable(AbilityTo ability) {
        return !(cooldowns.getOrDefault(ability, 0) > 0);
    }

    public void resetCooldowns() {
        cooldowns.clear();
    }
}
