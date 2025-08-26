package com.quest.entity.factory;

import com.quest.entity.Ability;

public class AbilityFactory {
    public static Ability createDamageAbility(String name, String description, int level, int damage, int cooldown) {
        return Ability.builder()
                .name(name)
                .type(Ability.AbilityType.DAMAGE)
                .description(description)
                .level(level)
                .value(damage)
                .cooldown(cooldown)
                .build();
    }
}
