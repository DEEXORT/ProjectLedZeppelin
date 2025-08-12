package com.quest.entity;

import com.quest.util.EventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {
    private Long id;
    private EventType type;
    private int value;
    private String stat; // for buff/debuff
    private int chance;
}

