package com.quest.dto;

import com.quest.entity.character.MonsterType;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class MonsterTo extends CharacterTo {
    MonsterType type;
}
