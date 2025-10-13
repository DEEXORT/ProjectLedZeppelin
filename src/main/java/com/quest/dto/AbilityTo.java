package com.quest.dto;

import com.quest.entity.AbilityType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbilityTo {
    Long id;
    String name;
    String description;
    AbilityType type;
    Integer level;
    Integer value;
    Integer cooldown;
    Integer levelRequirement;
}
