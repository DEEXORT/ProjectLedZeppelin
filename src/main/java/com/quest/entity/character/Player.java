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
    Long userId; // To bind to the user, because user overwrites player_id
    Long questSceneId;
    @Builder.Default
    int experiencePoints = 0;
    @Builder.Default
    int experienceLevel = 100;

    public void attack(Character character) {
        character.setHealth(character.getHealth() - experiencePoints);
    }
}
