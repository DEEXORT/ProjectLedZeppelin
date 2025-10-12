package com.quest.config;

import com.quest.services.hibernate.HibernateAbilityService;
import com.quest.services.hibernate.HibernateMonsterService;
import com.quest.services.hibernate.HibernateQuestService;
import com.quest.services.hibernate.HibernateUserService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@AllArgsConstructor
public class ConfigApplication {
    private final HibernateUserService userService;
    private final HibernateMonsterService monsterService;
    private final HibernateAbilityService abilityService;
    private final AbilityConfigLoader abilityConfigLoader;
    private final HibernateQuestService questService;
    private final MigrationDB migrationDB;
    private static final Logger logger = LogManager.getLogger(ConfigApplication.class);

    public void initApplication() {
        try {
            migrationDB.start();
        } catch (Exception e) {
            throw new RuntimeException("Error starting migration database", e);
        }
        if (abilityService.getAll().isEmpty()) {
            fillAbilitiesRepository();
        }
        if (questService.getAll().isEmpty()) {
            questService.fillQuestRepository();
            monsterService.fillMonsterRepository();
        }
    }

    public void fillAbilitiesRepository() {
        abilityConfigLoader.loadAbilities();
    }
}
