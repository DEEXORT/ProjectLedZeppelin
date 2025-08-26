package com.quest.entity.character;

import com.quest.entity.Ability;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
public class Monster extends Character {
    public enum MonsterType {
        BOSS, MINI_BOSS, ELITE, COMMON
    }
    MonsterType type;
}
