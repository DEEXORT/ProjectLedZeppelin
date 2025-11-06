package com.quest.dto;

import com.quest.entity.QuestSceneType;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class QuestSceneTo {
    Long id;
    Long fileId;
    String nameScene;
    String descriptionScene;
    @Builder.Default
    List<ActionTo> actions = new ArrayList<>();
    AchievementTo achievement;
    QuestSceneType type;
}
