package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.repository.RepositoryImpl;
import jakarta.transaction.Transactional;

import java.util.Collection;
import java.util.Optional;

public class HibernateAbilityService extends AbstractBaseService<Ability> {

    public HibernateAbilityService() {
        super(ServiceLocator.getService(RepositoryImpl.class, Ability.class));
    }

    public HibernateAbilityService(RepositoryImpl<Ability> repository) {
        super(repository);
    }

    // TODO: Добавить метод find в RepositoryImpl и переписать этот метод
    public Ability getByName(String name) {
        return getAll().stream()
                .filter(ability -> ability.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
