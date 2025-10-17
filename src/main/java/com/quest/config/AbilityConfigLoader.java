package com.quest.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.dto.AbilityTo;
import com.quest.services.hibernate.HibernateAbilityService;
import com.quest.util.ResourcePath;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class AbilityConfigLoader {
    private final HibernateAbilityService abilityService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Data
    private static class AbilityWrapper {
        @JsonProperty("abilities")
        List<AbilityTo> abilities = new ArrayList<>();
    }

    public void loadAbilities() {
        log.info("Loading Abilities");
        try {
            URL abilitiesConfig = getClass().getResource(ResourcePath.ABILITIES);
            AbilityWrapper wrapper = mapper.readValue(abilitiesConfig, AbilityWrapper.class);

            wrapper.getAbilities().forEach(abilityTo -> {
                abilityService.create(abilityTo);
                log.info("Created Ability: {}", abilityTo.getName());
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ability config", e);
        }
    }
}
