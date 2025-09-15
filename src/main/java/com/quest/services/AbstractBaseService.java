package com.quest.services;

import com.quest.repository.Repository;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
public class AbstractBaseService<T> implements BaseService<T>{
    private final Repository<T> repository;

    @Override
    public Optional<T> get(long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Collection<T> getAll() {
        return repository.getAll();
    }

    @Override
    public void create(T entity) {
        repository.create(entity);
    }

    @Override
    public void update(T entity) {
        repository.update(entity);
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }
}
