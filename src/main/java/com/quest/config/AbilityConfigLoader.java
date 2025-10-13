package com.quest.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.dto.AbilityConfigDTO;
import com.quest.dto.AbilityTo;
import com.quest.entity.AbilityType;
import com.quest.services.hibernate.HibernateAbilityService;
import com.quest.util.ResourcePath;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class AbilityConfigLoader {
    private static final Logger logger = LogManager.getLogger(AbilityConfigLoader.class);
    private final HibernateAbilityService abilityService;

    public AbilityConfigLoader(HibernateAbilityService abilityService) {
        this.abilityService = abilityService;
    }

    private final URL abilitiesConfig = getClass().getResource(ResourcePath.ABILITIES);

    public void loadAbilities() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            AbilityConfig config = mapper.readValue(abilitiesConfig, AbilityConfig.class);
            config.getAbilities().forEach(abilityDTO -> {
                AbilityTo ability = convertToEntity(abilityDTO);
                abilityService.create(ability);
                logger.info("Created Ability: {}", ability.getName());
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ability config", e);
        }
    }

    @Getter
    static class AbilityConfig {
        private List<AbilityConfigDTO> abilities;
    }

    private AbilityTo convertToEntity(AbilityConfigDTO abilityDTO) {
        return AbilityTo.builder()
                .id(abilityDTO.getId())
                .name(abilityDTO.getName())
                .description(abilityDTO.getDescription())
                .type(AbilityType.valueOf(abilityDTO.getType().toUpperCase()))
                .level(abilityDTO.getLevel())
                .value(abilityDTO.getValue())
                .cooldown(abilityDTO.getCooldown())
                .levelRequirement(abilityDTO.getLevelRequirement())
                .build();
    }
}
