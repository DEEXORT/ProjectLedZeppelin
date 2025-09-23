package com.quest.services.hibernate;

import java.util.Collection;
import java.util.Optional;

public interface BaseService<T> {
    Optional<T> get(long id);

    Collection<T> getAll();

    void create(T entity);

    void update(T entity);

    void delete(long id);
}
