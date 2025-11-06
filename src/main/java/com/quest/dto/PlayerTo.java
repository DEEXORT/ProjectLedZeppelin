package com.quest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class PlayerTo extends CharacterTo {
    Long userId;
    Long questSceneId;
    @Builder.Default
    int experiencePoints = 0;
    @Builder.Default
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
