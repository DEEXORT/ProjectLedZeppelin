package com.quest.util;

public class Const {
    // Paths
    public static final String PATH_QUEST_JSP = "/WEB-INF/quest-template.jsp";
    public static final String PATH_INDEX_JSP = "/WEB-INF/index.jsp";
    public static final String PATH_LOGIN_JSP = "/WEB-INF/login.jsp";
    public static final String PATH_REGISTER_JSP = "/WEB-INF/registration.jsp";
    public static final String PATH_PROFILE_JSP = "/WEB-INF/profile.jsp";
    public static final String PATH_ERROR_JSP = "/WEB-INF/error.jsp";
    public static final String PATH_BATTLE_JSP = "/WEB-INF/battle.jsp";
    public static final String PATH_END_GAME_JSP = "/WEB-INF/end-game.jsp";
    public static final String PATH_LEADER_BOARD_JSP = "/WEB-INF/leader-board.jsp";

    // Parse constants
    public static final String SCENE_DELIMITER = "##";
    public static final String ACTION_DELIMITER = "\\*";
    public static final String SCENE_TEXT_DELIMITER = "\\$";
    public static final String TARGET_SCENE_DELIMITER = "->";
    public static final String ACHIEVEMENT_DELIMITER_START = "<ach>";
    public static final String ACHIEVEMENT_DELIMITER_END = "</ach>";
    public static final long ID_END_MIN = 900L;
    public static final long ID_END_MAX = 950L;
    public static final long ID_DEATH_MAX = 1000L;

    // Router
    public static final String ROUTE_INDEX = "";
    public static final String ROUTE_GAME = "/game";
    public static final String ROUTE_QUEST = "/quest";
    public static final String ROUTE_LOGIN = "/login";
    public static final String ROUTE_REGISTER = "/register";
    public static final String ROUTE_PROFILE = "/profile";
    public static final String ROUTE_LOGOUT = "/logout";
    public static final String ROUTE_BATTLE = "/battle";
    public static final String ROUTE_END = "/end";
    public static final String ROUTE_LEADER_BOARD = "/leaderboard";

    // Resources
    public static final String RESOURCE_IMG_FINISH = "/images/finish.png";
    public static final String RESOURCE_IMG_RIP = "/images/rip.png";
    public static final String RESOURCE_QUEST = "/questScenario.txt";
}
