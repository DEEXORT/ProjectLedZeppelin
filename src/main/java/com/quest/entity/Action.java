package com.quest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "actions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private Long eventId; // used by quest-template.jsp

    @Transient
    private Long questSceneId; // TODO: check for usage and delete. In DB already exists relation between Scene and Action

    @Column(name = "text")
    private String actionText;

    @Column(name = "next_scene_id")
    private Long nextQuestSceneId;
}
