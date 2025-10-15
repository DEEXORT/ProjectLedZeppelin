package com.quest.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.dto.AbilityTo;
import com.quest.services.hibernate.HibernateAbilityService;
import com.quest.util.ResourcePath;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class AbilityConfigLoader {
    private final HibernateAbilityService abilityService;

    public void loadAbilities() {
        ObjectMapper mapper = ServiceLocator.getService(ObjectMapper.class);
        try {
            URL abilitiesConfig = getClass().getResource(ResourcePath.ABILITIES);
            List<AbilityTo> abilities = mapper.readValue(abilitiesConfig, new TypeReference<List<AbilityTo>>() {});

            abilities.forEach(abilityTo -> {
                abilityService.create(abilityTo);
                log.info("Created Ability: {}", abilityTo.getName());
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ability config", e);
        }
    }
}
