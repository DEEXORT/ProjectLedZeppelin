package com.quest.entity.character;

import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.entity.BattleHistory;
import com.quest.services.AbilityService;
import com.quest.util.ResourceBundleManager;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Entity
@Table(name = "characters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder(toBuilder = true)
public abstract class Character {
    public static final Logger logger = LogManager.getLogger(Character.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long characterId; // TODO: rename to id
    @Column(name = "name")
    String name;
    @Column(name = "level")
    int level;
    @Column(name = "health")
    int health;
    @Column(name = "max_health")
    int maxHealth;
    @Column(name = "attack")
    int attack;
    @Builder.Default
    @Transient
    List<Ability> abilities = initBaseAbilities();
    @Builder.Default
    @Transient
    Map<Ability, Integer> cooldowns = new HashMap<>();

    public static List<Ability> initBaseAbilities() {
        List<Ability> abilities = new ArrayList<>();
        AbilityService abilityService = ServiceLocator.getService(AbilityService.class);
        Ability baseAttack = abilityService.getByName(ResourceBundleManager.getSetting("ability.base_attack_name"));
        abilities.add(baseAttack);
        return abilities;
    }

    public boolean isAbilityAvailable(Ability ability) {
        return !(cooldowns.getOrDefault(ability, 0) > 0);
    }

    public boolean useAbility(Ability ability, Character target, BattleHistory history) {
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

    public void updateCooldown(Ability ability) {
        if (cooldowns.getOrDefault(ability, 0) == 0) {
            return;
        }
        cooldowns.put(ability, ability.getCooldown() - 1);
    }

    private void useDefense(Ability ability) {

    }

    private void useHeal(Ability ability, Character character, BattleHistory history) {
        character.setHealth(Math.min(character.getHealth() + ability.getValue(), character.getMaxHealth()));
        history.saveAction(this, character, ability);
    }

    private void useDamage(Ability ability, Character target, BattleHistory history) {
        int damage = calculateDamage(ability);
        logger.debug("{} damage", damage);
        target.setHealth(target.getHealth() - damage);
        history.saveAction(this, target, ability);
    }

    private int calculateDamage(Ability ability) {
        return ability.getValue() + (this.getAttack() * ability.getLevel() / 10);
    }

    public Ability getBaseAttack() {
        logger.debug("Size abilities: {}", abilities.size());
        return abilities.get(0);
    }

    public void resetCooldowns() {
        cooldowns.clear();
    }
}
