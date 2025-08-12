package com.quest.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Action {
    private Long id;
    private Long eventId;
    private Long questSceneId; // TODO: Возможно лишнее поле
    private String actionText;
    private QuestScene questScene; // TODO: Возможно лишнее поле
    private QuestScene nextQuestScene; // TODO: Возможно лишнее поле
    private Long nextQuestSceneId;
}
