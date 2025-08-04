package com.quest.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Monster {
    private Long id;
    private String name;
    private int level;
    private int health;
    private int maxHealth;
}
