package com.quest.services.hibernate;

import java.util.Collection;

public interface BaseService<T> {
    T get(long id);

    Collection<T> getAll();

    void create(T dto);

    void update(T dto);

    void delete(T dto);
}
