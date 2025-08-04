package com.quest.entity;

import com.quest.util.StatusPlayer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStat {
    private User user;
    private Player player;
    private String achievementText;
    private StatusPlayer status;
}
