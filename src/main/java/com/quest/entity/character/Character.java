package com.quest.entity.character;

import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.entity.BattleHistory;
import com.quest.services.AbilityService;
import com.quest.services.hibernate.HibernateAbilityService;
import com.quest.util.ResourceBundleManager;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Column(name = "name", nullable = false, length = 50)
    String name;
    @Column(name = "level", nullable = false)
    int level;
    @Column(name = "health", nullable = false)
    int health;
    @Column(name = "max_health", nullable = false)
    int maxHealth;
    @Column(name = "attack", nullable = false)
    int attack;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "character_ability",
            joinColumns = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "ability_id"))
    List<Ability> abilities = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "cooldowns",
            joinColumns = @JoinColumn(name = "character_id"))
    @MapKeyJoinColumn(name = "ability_id")
    @Column(name = "cooldown_value")
    Map<Ability, Integer> cooldowns = new HashMap<>();

    public List<Ability> initBaseAbilities() {
        List<Ability> abilities = new ArrayList<>();
//        AbilityService abilityService = ServiceLocator.getService(AbilityService.class);
        HibernateAbilityService abilityService = ServiceLocator.getService(HibernateAbilityService.class);
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
