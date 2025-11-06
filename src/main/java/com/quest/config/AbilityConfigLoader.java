package com.quest.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.dto.AbilityTo;
import com.quest.services.hibernate.HibernateAbilityService;
import com.quest.util.ResourcePath;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Slf4j
public class AbilityConfigLoader {
    private final HibernateAbilityService abilityService;

    public AbilityConfigLoader(HibernateAbilityService abilityService) {
        this.abilityService = abilityService;
    }

    private final URL abilitiesConfig = getClass().getResource(ResourcePath.ABILITIES);

    public void loadAbilities() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            AbilityConfig config = mapper.readValue(abilitiesConfig, AbilityConfig.class);
            config.getAbilities().forEach(abilityTo -> {
                abilityService.create(abilityTo);
                log.debug("Created Ability: {}", abilityTo.getName());
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ability config", e);
        }
    }

    @Getter
    static class AbilityConfig {
        private List<AbilityTo> abilities;
    }
}
