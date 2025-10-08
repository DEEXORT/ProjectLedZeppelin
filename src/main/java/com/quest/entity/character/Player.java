package com.quest.entity.character;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
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
    // no usages
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long playerId;
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

    public void increaseExperience(int experience) {
        int experienceLevel = this.getExperienceLevel();
        this.setExperiencePoints(this.getExperiencePoints() + experience);

        if (this.getExperiencePoints() >= experienceLevel) {
            this.setLevel(this.getLevel() + 1);
            this.setExperienceLevel(2 * experienceLevel);
            this.setExperiencePoints(this.getExperiencePoints() - experienceLevel);
            this.setMaxHealth(2 * this.getMaxHealth());
            this.setHealth(this.getMaxHealth());
        }
    }
}
