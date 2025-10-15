package com.quest.config;

import com.quest.services.hibernate.HibernateAbilityService;
import com.quest.services.hibernate.HibernateMonsterService;
import com.quest.services.hibernate.HibernateQuestService;
import com.quest.services.hibernate.HibernateUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ConfigApplication {
    private final HibernateUserService userService;
    private final HibernateMonsterService monsterService;
    private final HibernateAbilityService abilityService;
    private final AbilityConfigLoader abilityConfigLoader;
    private final HibernateQuestService questService;
    private final MigrationDB migrationDB;

    public void initApplication() {
        try {
            migrationDB.start();
        } catch (Exception e) {
            log.error(e.getMessage());
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
