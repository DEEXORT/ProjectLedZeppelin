package com.quest.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Action {
    private Long id;
    private Long eventId;
    private Long questSceneId;
    private String actionText;
    private Long nextQuestSceneId;
}
