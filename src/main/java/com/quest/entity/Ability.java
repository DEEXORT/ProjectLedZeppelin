package com.quest.entity;

import com.quest.entity.character.Character;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ability {
    public enum AbilityType {
        DAMAGE, HEAL, DEFENSE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    AbilityType type;

    @Column(name = "level")
    int level;

    @Column(name = "value")
    int value;

    @Column(name = "cooldown")
    int cooldown;

    @Column(name = "level_requirement")
    int levelRequirement;

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "character_ability",
    joinColumns = @JoinColumn(name = "ability_id"),
    inverseJoinColumns = @JoinColumn(name = "character_id"))
    private List<Character> characters = new ArrayList<>();
}
