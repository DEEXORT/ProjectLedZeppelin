package com.quest.entity.character;

import com.quest.entity.Ability;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
public class Player extends Character {
    Long playerId;
    Long userId; // To bind to the user, because user overwrites player_id
    Long questSceneId;
    @Builder.Default
    int experiencePoints = 0;
    @Builder.Default
    int experienceLevel = 100;

//    public void attack(Character character) {
//        character.setHealth(character.getHealth() - experiencePoints);
//    }

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
