package com.quest.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Action {
    private Long id;
    private Long eventId; // used by quest-template.jsp
    private Long questSceneId; // TODO: check for usage
    private String actionText;
    private Long nextQuestSceneId;
}
