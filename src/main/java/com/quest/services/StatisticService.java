package com.quest.services;

import com.quest.entity.character.Player;
import com.quest.entity.QuestScene;
import com.quest.entity.User;
import com.quest.entity.UserStat;
import com.quest.util.ParseConst;
import com.quest.util.StatusPlayer;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class StatisticService {
    private final UserService userService;
    private final QuestService questService;
    private final PlayerService playerService;

    public List<UserStat> getUserStats() {
        List<UserStat> userStats = new ArrayList<>();
        // Перебираем всех игроков из БД
        for (Player player : playerService.getAll()) {
            // Получаем пользователя, за которым закреплен игрок
            Optional<User> user = userService.get(player.getId());
            if (user.isPresent()) {
                // Получаем сцену, на которой закончил игрок
                Optional<QuestScene> questScene = questService.get(player.getQuestSceneId());
                if (questScene.isPresent() ) {
                    // Игрок в прохождении игры
                    UserStat stat = UserStat.builder()
                            .user(user.get())
                            .player(player)
                            .achievementText("-")
                            .status(StatusPlayer.IN_PROGRESS)
                            .build();

                    Long questSceneId = questScene.get().getId();
                    if (questSceneId >= ParseConst.ID_END_MIN && questSceneId <= ParseConst.ID_END_MAX) {
                        // Если игрок завершил игру с достижением
                        stat.setAchievementText(questScene.get().getAchievement().getText());
                        stat.setStatus(StatusPlayer.FINISHED);
                    } else if (questSceneId > ParseConst.ID_END_MAX) {
                        // Если игрок погиб
                        stat.setStatus(StatusPlayer.DEATH);
                    }
                    userStats.add(stat);
                }
            }
        }
        return userStats;
    }
}
