package com.quest.mapping;

import com.quest.dto.AbilityTo;
import com.quest.dto.AchievementTo;
import com.quest.dto.ActionTo;
import com.quest.dto.CharacterTo;
import com.quest.dto.EventTo;
import com.quest.dto.MonsterTo;
import com.quest.dto.PlayerTo;
import com.quest.dto.QuestSceneTo;
import com.quest.dto.UserTo;
import com.quest.entity.Ability;
import com.quest.entity.Achievement;
import com.quest.entity.Action;
import com.quest.entity.Event;
import com.quest.entity.QuestScene;
import com.quest.entity.User;
import com.quest.entity.character.Monster;
import com.quest.entity.character.Player;
import org.mapstruct.Mapper;
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
