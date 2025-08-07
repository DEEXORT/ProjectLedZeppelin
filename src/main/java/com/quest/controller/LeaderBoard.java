package com.quest.controller;

import com.quest.config.ServiceLocator;
import com.quest.entity.Player;
import com.quest.entity.QuestScene;
import com.quest.entity.User;
import com.quest.entity.UserStat;
import com.quest.services.PlayerService;
import com.quest.services.QuestService;
import com.quest.services.UserService;
import com.quest.util.*;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(Route.LEADER_BOARD)
public class LeaderBoard extends HttpServlet {
    private PlayerService playerService;
    private QuestService questService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) {
        playerService = ServiceLocator.getService(PlayerService.class);
        userService = ServiceLocator.getService(UserService.class);
        questService = ServiceLocator.getService(QuestService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<UserStat> userStats = new ArrayList<>();
        for (Player player : playerService.getAll()) {
            Optional<User> user = userService.get(player.getUserId());
            if (user.isPresent()) {
                Optional<QuestScene> questScene = questService.get(player.getQuestSceneId());
                if (questScene.isPresent() ) {
                    UserStat stat;
                    Long questSceneId = questScene.get().getId();
                    if (questSceneId >= ParseConst.ID_END_MIN && questSceneId <= ParseConst.ID_END_MAX) {
                        // Если игрок завершил игру с достижением
                        stat = UserStat.builder()
                                .user(user.get())
                                .player(player)
                                .achievementText(questScene.get().getAchievement().getText())
                                .status(StatusPlayer.FINISHED)
                                .build();
                    } else if (questSceneId > ParseConst.ID_END_MAX) {
                        // Если игрок погиб
                        stat = UserStat.builder()
                                .user(user.get())
                                .player(player)
                                .achievementText("-")
                                .status(StatusPlayer.DEATH)
                                .build();
                    } else {
                        // Если игрок еще в процессе прохождения
                        stat = UserStat.builder()
                                .user(user.get())
                                .player(player)
                                .achievementText("-")
                                .status(StatusPlayer.IN_PROGRESS)
                                .build();
                    }
                    userStats.add(stat);
                }
            }
        }

        req.setAttribute(KeyAttribute.STATS, userStats);
        req.getRequestDispatcher(JspPath.LEADER_BOARD).forward(req, resp);
    }
}
