package com.quest.services;

import com.quest.entity.Ability;
import com.quest.repository.AbilityRepository;
import com.quest.services.hibernate.BaseService;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
public class AbilityService implements BaseService<Ability> {
    private final AbilityRepository repository;

    public void create(Ability ability) {
        repository.create(ability);
    }

    @Override
    public Optional<Ability> get(long id) {
        return Optional.ofNullable(repository.get(id));
    }

    public Ability getAbility(long id) {
        return repository.get(id);
    }

    public Ability getByName(String name) {
        return getAll().stream()
                .filter(ability -> ability.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Collection<Ability> getAll() {
        return repository.getAll();
    }

    public void update(Ability ability) {
        repository.update(ability);
    }

    public void delete(long id) {
        repository.delete(id);
    }
}
