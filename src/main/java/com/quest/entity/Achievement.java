package com.quest.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Achievement {
    private Long id;
    private Long questSceneId;
    private String text;
}
