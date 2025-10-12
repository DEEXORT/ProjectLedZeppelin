package com.quest.repository;

import java.util.Collection;
import java.util.stream.Stream;

public interface Repository<T> {
    Collection<T> getAll();

    T get(long id);

    void create(T object);

    void update(T object);

    void delete(T object);

    Stream<T> find(T object);
}
