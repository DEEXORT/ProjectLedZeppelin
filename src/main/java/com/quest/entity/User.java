package com.quest.entity;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String login;
    private String password;
    private Long playerId;
    @Builder.Default
    private List<Achievement> achievements = new ArrayList<>();
}
