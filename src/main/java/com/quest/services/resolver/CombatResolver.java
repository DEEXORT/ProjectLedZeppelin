package com.quest.services.resolver;

import com.quest.entity.Monster;
import com.quest.entity.Player;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@NoArgsConstructor
public class CombatResolver {
    public Random random = new Random();

    public void resolveCombat(Player player, Monster monster) {
        int chanceWinPlayer = random.nextInt(100);

        if (chanceWinPlayer < 20) { // TODO: вернуть на 20
            // Поражение. Игрок помер
            player.setHealth(0);
        } else {
            // Чем выше вероятность, тем меньше хп потеряет
//            player.setHealth(player.getHealth() - (100 - chanceWinPlayer));
            player.setHealth(player.getHealth() - 10);
            player.setLevel(player.getLevel() + 1);
            // Победа. Монстр помер
            monster.setHealth(0);
        }
    }
}
