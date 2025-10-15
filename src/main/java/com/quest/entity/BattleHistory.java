package com.quest.entity;

import com.quest.dto.AbilityTo;
import com.quest.dto.CharacterTo;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class BattleHistory {
    @Getter
    List<String> history = new LinkedList<>();

    public void saveAction(CharacterTo attacker, CharacterTo target, AbilityTo ability, int value) {
        switch (ability.getType()) {
            case DAMAGE ->
                    history.add("<b>%s</b> применяет способность \"<b>%s</b>\" и наносит урон <b>%s</b> в размере %d."
                            .formatted(attacker.getName(), ability.getName(), target.getName(), value));
            case HEAL ->
                    history.add("<b>%s</b> применяет способность \"<b>%s</b>\" и восстанавливает здоровье <b>%s</b> в размере %d."
                            .formatted(attacker.getName(), ability.getName(), target.getName(), value));
            case DEFENSE ->
                    history.add("<b>%s</b> применяет способность \"<b>%s</b>\" и увеличивает защиту <b>%s</b> в размере %d."
                            .formatted(attacker.getName(), ability.getName(), target.getName(), value));
        }
    }
}
