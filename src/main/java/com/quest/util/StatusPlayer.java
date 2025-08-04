package com.quest.util;

public enum StatusPlayer {
    FINISHED("Игра пройдена"),
    DEATH("Помер"),
    IN_PROGRESS("В процессе игры");

    private final String name;

    StatusPlayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
