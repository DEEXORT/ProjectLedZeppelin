package com.quest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "scenes")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestScene {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private Long fileId;

    @Transient
    private String nameScene;

    @Column(name = "description")
    private String descriptionScene;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "scene_id")
    private List<Action> actions = new ArrayList<>(); // TODO: Need to get from ActionRepository instead of QuestRepository

    @OneToOne
    @JoinColumn(name = "id")
    private Achievement achievement;
}
