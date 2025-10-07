package com.quest.repository;

import java.util.Collection;

public interface Repository<T> {
    Collection<T> getAll();
    T get(long id);
    void create(T object);
    void update(T object);
    void delete(T object);
}
