package com.quest.services.resolver;

import com.quest.entity.Ability;
import com.quest.entity.character.Monster;
import com.quest.entity.character.Player;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
public class BattleResolver {
    private ThreadLocalRandom randomLocal;

    public BattleResolver(ThreadLocalRandom randomLocal) {
        this.randomLocal = randomLocal;
    }

    public void resolveBattle(Player player, Monster monster) {
        int chanceWinPlayer = randomLocal.nextInt(100);

        if (chanceWinPlayer < 20) {
            // Поражение. Игрок помер
            player.setHealth(0);
        } else {
            // Чем выше вероятность, тем меньше хп потеряет
            player.setHealth(player.getHealth() - 10);
            player.setLevel(player.getLevel() + 1);
            // Победа. Монстр помер
            monster.setHealth(0);
        }
    }

    public void attack(Player player, Monster monster, Ability abilityPlayer) {
        player.useAbility(abilityPlayer, monster);
    }
}
