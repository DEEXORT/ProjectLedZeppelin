package com.quest.dto;

import com.quest.entity.AbilityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
