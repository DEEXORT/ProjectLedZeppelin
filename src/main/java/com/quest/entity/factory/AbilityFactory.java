package com.quest.entity.factory;

import com.quest.dto.AbilityTo;
import com.quest.entity.AbilityType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AbilityFactory {

    public AbilityTo createDamageAbility(String name, String description, int level, int damage, int cooldown) {
        return AbilityTo.builder()
                .name(name)
                .type(AbilityType.DAMAGE)
                .description(description)
                .level(level)
                .value(damage)
                .cooldown(cooldown)
                .build();
    }
}
