package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.dto.AbilityTo;
import com.quest.dto.PlayerTo;
import com.quest.entity.Ability;
import com.quest.mapping.Dto;
import com.quest.repository.RepositoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class HibernateAbilityService {
    private RepositoryImpl<Ability> repository;
    private final Logger logger = LogManager.getLogger(HibernateAbilityService.class);
    private Dto dto = Dto.MAPPER;

    public HibernateAbilityService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, Ability.class);
    }

    public HibernateAbilityService(RepositoryImpl<Ability> repository) {
        this.repository = repository;
    }

    public void create(AbilityTo abilityTo) {
        repository.create(dto.from(abilityTo));
    }

    public void update(AbilityTo abilityTo) {
        repository.update(dto.from(abilityTo));
    }

    public Optional<AbilityTo> get(long id) {
        return Optional.ofNullable(repository.get(id)).map(dto::from);
    }

    public Collection<AbilityTo> getAll() {
        return repository.getAll().stream().map(dto::from).collect(Collectors.toList());
    }

    // TODO: Добавить метод find в RepositoryImpl и переписать этот метод
    public AbilityTo getByName(String abilityName) {
        Ability ability = new Ability();
        ability.setName(abilityName);
        Optional<AbilityTo> optAbility = repository.find(ability)
                .map(dto::from).findFirst();
        if (optAbility.isPresent()) {
            return optAbility.get();
        } else {
            logger.warn("Ability not found");
            throw new RuntimeException("Ability not found");
        }
    }

    public void delete(AbilityTo ability) {
        repository.delete(dto.from(ability));
    }
}
