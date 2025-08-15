package com.quest.entity;

import com.quest.util.EventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {
    // -------Required Fields---------
    private Long id;
    private EventType type;
    private String description;
    // -------------------------------
    private String stat; // for type = buff/debuff
    private Long monsterId; // for type = battle
    private int value; // for type = damage/heal/buff/debuff

    private int chance; // no usages (temp)
}

