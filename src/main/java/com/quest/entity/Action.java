package com.quest.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Action {
    private Long id;
    private Long questSceneId;
    private String actionText;
    private QuestScene questScene;
    private QuestScene nextQuestScene;
    private Long nextQuestSceneId;
    private Event event;
}
