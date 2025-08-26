package com.quest.services;

import com.quest.entity.Ability;
import com.quest.repository.AbilityRepository;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class AbilityService {
    private final AbilityRepository repository;

    public void create(Ability ability) {
        repository.create(ability);
    }

    public Ability get(long id) {
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
