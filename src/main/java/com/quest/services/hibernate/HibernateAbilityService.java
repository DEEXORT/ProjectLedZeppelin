package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.dto.AbilityTo;
import com.quest.entity.Ability;
import com.quest.mapping.Dto;
import com.quest.repository.RepositoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class HibernateAbilityService implements BaseService<AbilityTo> {
    private RepositoryImpl<Ability> repository;
    private Dto dto = Dto.MAPPER;

    public HibernateAbilityService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, Ability.class);
    }

    public void create(AbilityTo abilityTo) {
        Ability ability = dto.from(abilityTo);
        repository.create(ability);
        abilityTo.setId(ability.getId());
    }

    public void update(AbilityTo abilityTo) {
        repository.update(dto.from(abilityTo));
    }

    public AbilityTo get(long id) {
        return dto.from(repository.get(id));
    }

    public Collection<AbilityTo> getAll() {
        return repository.getAll().stream().map(dto::from).collect(Collectors.toList());
    }

    public AbilityTo getByName(String abilityName) {
        Ability ability = new Ability();
        ability.setName(abilityName);
        Optional<AbilityTo> optAbility = repository.find(ability)
                .map(dto::from).findFirst();
        if (optAbility.isPresent()) {
            return optAbility.get();
        } else {
            log.warn("Ability not found");
            throw new RuntimeException("Ability not found");
        }
    }

    public void delete(AbilityTo ability) {
        repository.delete(dto.from(ability));
    }
}
