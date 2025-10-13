package com.quest.services;

import com.quest.dto.PlayerTo;
import com.quest.dto.QuestSceneTo;
import com.quest.dto.UserTo;
import com.quest.entity.QuestSceneType;
import com.quest.entity.UserStat;
import com.quest.entity.character.Player;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.services.hibernate.HibernateQuestService;
import com.quest.services.hibernate.HibernateUserService;
import com.quest.util.StatusPlayer;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class StatisticService {
    private final HibernateUserService userService;
    private final HibernateQuestService questService;
    private final HibernatePlayerService playerService;

    public List<UserStat> getUserStats() {
        List<UserStat> userStats = new ArrayList<>();
        // Перебираем всех игроков из БД
        for (PlayerTo player : playerService.getAll()) {
            // Получаем пользователя, за которым закреплен игрок
            Optional<UserTo> user = userService.get(player.getUserId());
            if (user.isPresent()) {
                // Получаем сцену, на которой закончил игрок
                Optional<QuestSceneTo> questScene = questService.get(player.getQuestSceneId());
                if (questScene.isPresent()) {
                    // Игрок в прохождении игры
                    UserStat stat = UserStat.builder()
                            .user(user.get())
                            .player(player)
                            .achievementText("-")
                            .status(StatusPlayer.IN_PROGRESS)
                            .build();

                    Long questSceneId = questScene.get().getId();
                    QuestSceneType typeScene = questScene.get().getType();
                    if (typeScene == QuestSceneType.COMPLETE) {
                        // Если игрок завершил игру с достижением
                        stat.setAchievementText(questScene.get().getAchievement().getText());
                        stat.setStatus(StatusPlayer.FINISHED);
                    } else if (typeScene == QuestSceneType.DEATH
                            || typeScene == QuestSceneType.BATTLE_DEATH) {
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
