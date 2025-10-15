package com.quest.entity;

import com.quest.entity.character.Character;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "abilities")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ability {

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
    Integer level;

    @Column(name = "value")
    Integer value;

    @Column(name = "cooldown")
    Integer cooldown;

    @Column(name = "level_requirement")
    Integer levelRequirement;

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "character_ability",
            joinColumns = @JoinColumn(name = "ability_id"),
            inverseJoinColumns = @JoinColumn(name = "character_id"))
    private List<Character> characters = new ArrayList<>();

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ability ability = (Ability) obj;
        return Objects.equals(id, ability.id) && Objects.equals(name, ability.name);
    }
}
