package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.Ability;
import com.quest.repository.RepositoryImpl;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class HibernateAbilityService implements BaseService<Ability>{
    private final RepositoryImpl<Ability> repository;

    public HibernateAbilityService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, Ability.class);
    }

    @Override
    public Optional<Ability> get(long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Collection<Ability> getAll() {
        return repository.getAll();
    }

    @Override
    @Transactional
    public void create(Ability entity) {
        repository.create(entity);
    }

    @Override
    @Transactional
    public void update(Ability entity) {
        repository.update(entity);
    }

    @Override
    @Transactional
    public void delete(long id) {
        repository.delete(id);
    }

    // TODO: Добавить метод find в RepositoryImpl и переписать этот метод
    public Ability getByName(String name) {
        return getAll().stream()
                .filter(ability -> ability.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
