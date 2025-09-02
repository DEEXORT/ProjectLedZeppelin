package com.quest.dto;

import lombok.Data;

@Data
public class AbilityConfigDTO {
    private Long id;
    private String unikey;
    private String name;
    private String description;
    private String type;
    private int level;
    private int value;
    private int cooldown;
    private int levelRequirement;
}
