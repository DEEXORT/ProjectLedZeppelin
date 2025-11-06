package com.quest.entity.character;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
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
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    MonsterType type;
}
