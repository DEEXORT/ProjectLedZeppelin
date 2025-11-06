package com.quest.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "scenes")
@Setter
@Getter
@Entity
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

    @Fetch(FetchMode.JOIN)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "scene_id")
    private List<Action> actions = new ArrayList<>(); // TODO: Need to get from ActionRepository instead of QuestRepository

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "achievement_id")
    private Achievement achievement;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private QuestSceneType type;
}
