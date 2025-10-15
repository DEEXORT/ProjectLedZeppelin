package com.quest.mapping;

import com.quest.dto.*;
import com.quest.entity.*;
import com.quest.entity.character.Monster;
import com.quest.entity.character.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Dto {
    Dto MAPPER = Mappers.getMapper(Dto.class);

    Character from(CharacterTo characterTo);
    CharacterTo from(Character characterTo);

    User from(UserTo user);
    UserTo from(User user);

    Achievement from(AchievementTo achievementTo);
    AchievementTo from(Achievement achievementTo);

    QuestScene from(QuestSceneTo questSceneTo);
    QuestSceneTo from(QuestScene questSceneTo);

    Action from(ActionTo actionTo);
    ActionTo from(Action actionTo);

    Ability from(AbilityTo abilityTo);
    AbilityTo from(Ability abilityTo);

    Player from(PlayerTo playerTo);
    PlayerTo from(Player playerTo);

    Monster from(MonsterTo monsterTo);
    MonsterTo from(Monster monsterTo);

    Event from(EventTo eventTo);
    EventTo from(Event eventTo);
}
