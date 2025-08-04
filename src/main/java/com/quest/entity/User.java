package com.quest.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {
    private Long id;
    private String login;
    private String password;
    private Long playerId;
    private List<Achievement> achievements;
}
