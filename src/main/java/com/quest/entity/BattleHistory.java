package com.quest.entity;

import com.quest.entity.character.Character;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public class BattleHistory {
    @Getter
    List<String> history = new LinkedList<>();

    public void saveAction(Character attacker, Character target, Ability ability, int value) {
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
