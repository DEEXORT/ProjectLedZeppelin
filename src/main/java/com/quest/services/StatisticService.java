package com.quest.services;

import com.quest.dto.PlayerTo;
import com.quest.dto.QuestSceneTo;
import com.quest.dto.UserTo;
import com.quest.entity.QuestSceneType;
import com.quest.entity.UserStat;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.services.hibernate.HibernateQuestService;
import com.quest.services.hibernate.HibernateUserService;
import com.quest.util.StatusPlayer;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class StatisticService {
    private final HibernateUserService userService;
    private final HibernateQuestService questService;
    private final HibernatePlayerService playerService;

    public List<UserStat> getUserStats() {
        List<UserStat> userStats = new ArrayList<>();
        // Iterate all players from database
        for (PlayerTo player : playerService.getAll()) {
            // Getting the user from player
            UserTo user = userService.get(player.getUserId());
            if (user != null) {
                // Getting the scene where player stopped
                QuestSceneTo questScene = questService.get(player.getQuestSceneId());
                if (questScene != null) {
                    // Player in progress game
                    UserStat stat = UserStat.builder()
                            .user(user)
                            .player(player)
                            .achievementText("-")
                            .status(StatusPlayer.IN_PROGRESS)
                            .build();

                    Long questSceneId = questScene.getId();
                    QuestSceneType typeScene = questScene.getType();
                    if (typeScene == QuestSceneType.COMPLETE) {
                        // If player ended the game with achievement
                        stat.setAchievementText(questScene.getAchievement().getText());
                        stat.setStatus(StatusPlayer.FINISHED);
                    } else if (typeScene == QuestSceneType.DEATH
                            || typeScene == QuestSceneType.BATTLE_DEATH) {
                        // If player died
                        stat.setStatus(StatusPlayer.DEATH);
                    }
                    userStats.add(stat);
                }
            }
        }
        return userStats;
    }
}
