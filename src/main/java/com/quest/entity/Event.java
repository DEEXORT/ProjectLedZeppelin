package com.quest.entity;

import com.quest.util.EventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
@Builder
public class Event {
    // TODO: needs to be divided into subclasses
    // -------Required Fields---------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;

    @Column(name = "description", nullable = false)
    private String description;
    // -------------------------------
    @Column(name = "stat")
    private String stat; // for type = buff/debuff

    @Column(name = "monster_id")
    private Long monsterId; // for type = battle

    @Column(name = "value")
    private int value; // for type = damage/heal/buff/debuff

    @Transient
    private int chance; // no usages (temp)
}

