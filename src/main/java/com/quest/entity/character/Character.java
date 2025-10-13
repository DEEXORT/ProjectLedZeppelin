package com.quest.entity.character;

import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.entity.BattleHistory;
import com.quest.services.hibernate.HibernateAbilityService;
import com.quest.util.ResourceBundleManager;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
    Long id;
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

    @Fetch(FetchMode.JOIN)
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "character_ability",
            joinColumns = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "ability_id"))
    List<Ability> abilities = new ArrayList<>();

    @Fetch(FetchMode.JOIN)
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "cooldowns",
            joinColumns = @JoinColumn(name = "character_id"))
    @MapKeyJoinColumn(name = "ability_id")
    @Column(name = "cooldown_value")
    Map<Ability, Integer> cooldowns = new HashMap<>();

}
