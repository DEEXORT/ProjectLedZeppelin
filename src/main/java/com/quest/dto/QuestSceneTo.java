package com.quest.dto;

import com.quest.entity.QuestSceneType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestSceneTo {
    Long id;
    Long fileId;
    String nameScene;
    String descriptionScene;
    List<ActionTo> actions;
    AchievementTo achievement;
    QuestSceneType type;
}
