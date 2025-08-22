package com.quest.config;

import com.quest.entity.character.Monster;
import com.quest.entity.character.Player;
import com.quest.entity.User;
import com.quest.entity.factory.MonsterFactory;
import com.quest.services.MonsterService;
import com.quest.services.PlayerService;
import com.quest.services.UserService;
import com.quest.util.QuestParser;
import com.quest.util.ResourcePath;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class ConfigApplication {
    private final UserService userService;
    private final MonsterService monsterService;
    private final PlayerService playerService;
    private static final Logger logger = LogManager.getLogger(ConfigApplication.class);

    public void initApplication() {
        fillQuestRepository();
        fillUserRepository();
        fillMonsterRepository();
    }

    private void fillPlayerRepository(User user) {
        playerService.create(Player.builder()
                        .name("Lukas")
                        .health(100)
                        .maxHealth(100)
                        .level(999)
                        .attack(10)
                        .questSceneId(902L)
                        .userId(user.getId())
                        .build());
    }


    private void fillQuestRepository() {
        QuestParser questParser = ServiceLocator.getService(QuestParser.class);
        try {
            URL resource = Objects.requireNonNull(
                    getClass().getResource(ResourcePath.QUEST));
            Path pathQuestFile = Paths.get(resource.toURI());
            String dataQuest = Files.readString(pathQuestFile, StandardCharsets.UTF_8);
            questParser.parseText(dataQuest);
        } catch (IOException | URISyntaxException e) {
            logger.error("Error reading quest file");
            throw new RuntimeException("Error reading quest file", e);
        }
        logger.info("Loaded quest repository");
    }

    private void fillUserRepository() {
        Optional<User> optionalUser = userService.get(1L);
        if (optionalUser.isEmpty()) {
            User user = User.builder()
                    .login("admin")
                    .password("admin")
                    .achievements(new ArrayList<>())
                    .id(1L)
                    .build();
            userService.create(user);
            fillPlayerRepository(user);
        }
    }

    private void fillMonsterRepository() {
        Monster goblin = MonsterFactory.createGoblinMonster();
        Monster orc = MonsterFactory.createOrcMonster();
        monsterService.create(goblin);
        monsterService.create(orc);
    }

}
