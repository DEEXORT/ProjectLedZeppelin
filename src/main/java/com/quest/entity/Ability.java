package com.quest.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ability {
    public enum AbilityType {
        DAMAGE, HEAL, DEFENSE
    }
    Long id;
    String name;
    String description;
    AbilityType type;
    int level;
    int value;
    int cooldown;
    int levelRequirement;
}
