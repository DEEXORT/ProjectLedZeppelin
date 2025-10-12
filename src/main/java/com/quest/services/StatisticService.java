package com.quest.services;

import com.quest.entity.QuestScene;
import com.quest.entity.User;
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
        for (Player player : playerService.getAll()) {
            // Получаем пользователя, за которым закреплен игрок
            Optional<User> user = userService.get(player.getUserId());
            if (user.isPresent()) {
                // Получаем сцену, на которой закончил игрок
                Optional<QuestScene> questScene = questService.get(player.getQuestSceneId());
                if (questScene.isPresent()) {
                    // Игрок в прохождении игры
                    UserStat stat = UserStat.builder()
                            .user(user.get())
                            .player(player)
                            .achievementText("-")
                            .status(StatusPlayer.IN_PROGRESS)
                            .build();

                    Long questSceneId = questScene.get().getId();
                    QuestScene.Type typeScene = questScene.get().getType();
                    if (typeScene == QuestScene.Type.COMPLETE) {
                        // Если игрок завершил игру с достижением
                        stat.setAchievementText(questScene.get().getAchievement().getText());
                        stat.setStatus(StatusPlayer.FINISHED);
                    } else if (typeScene == QuestScene.Type.DEATH
                            || typeScene == QuestScene.Type.BATTLE_DEATH) {
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
