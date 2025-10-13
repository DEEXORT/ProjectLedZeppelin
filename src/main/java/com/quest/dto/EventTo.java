package com.quest.dto;

import com.quest.util.EventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventTo {
    // TODO: needs to be divided into subclasses
    Long id;
    EventType type;
    String description;
    String stat; // for type = buff/debuff
    Long monsterId; // for type = battle
    int value; // for type = damage/heal/buff/debuff
}
