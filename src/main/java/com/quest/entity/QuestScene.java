package com.quest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestScene {
    private Long id;
    private String nameScene;
    private String descriptionScene;
    private List<Action> actions = new ArrayList<>(); // TODO: Need to get from ActionRepository instead of QuestRepository
    private Achievement achievement;
}
