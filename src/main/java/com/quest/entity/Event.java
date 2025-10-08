package com.quest.entity;

import com.quest.util.EventType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
@Builder
public class Event {
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

