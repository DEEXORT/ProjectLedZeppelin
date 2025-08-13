package com.quest.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {
    private Long id;
    private Long userId;
    private Long questSceneId;
    private String name;
    private int level;
    private int health;
    private int maxHealth;
    private int attack;
}
