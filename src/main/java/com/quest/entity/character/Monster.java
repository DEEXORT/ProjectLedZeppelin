package com.quest.entity.character;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "monsters")
@SuperBuilder(toBuilder = true)
@PrimaryKeyJoinColumn(name = "character_id")
public class Monster extends Character {
    public enum MonsterType {
        BOSS, MINI_BOSS, ELITE, COMMON
    }

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    MonsterType type;
}
