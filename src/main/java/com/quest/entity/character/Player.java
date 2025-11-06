package com.quest.entity.character;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "character_id")
@SuperBuilder(toBuilder = true)
public class Player extends Character {
    @Column(name = "user_id")
    Long userId; // To bind to the user, because user overwrites player_id
    @Column(name = "scene_id")
    Long questSceneId;
    @Builder.Default
    @Column(name = "experience_points")
    int experiencePoints = 0;
    @Builder.Default
    @Column(name = "experience_level")
    int experienceLevel = 100;


}
