package com.quest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionTo {
    Long id;
    Long eventId;
    String actionText;
    Long nextQuestSceneId;
}
