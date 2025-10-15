package com.quest.entity;

import com.quest.dto.PlayerTo;
import com.quest.dto.UserTo;
import com.quest.util.StatusPlayer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStat {
    private UserTo user;
    private PlayerTo player;
    private String achievementText;
    private StatusPlayer status;
}
