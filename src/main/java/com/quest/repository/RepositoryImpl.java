package com.quest.repository;

import com.quest.util.TransactionManager;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class RepositoryImpl<T> implements Repository<T> {
    private final TransactionManager transactionManager;
    private final Class<T> entityClass;

    @Override
    public Collection<T> getAll() {
        List<T> list = new ArrayList<>();
//        transactionManager.doInTransaction(() -> {
//            T entity = transactionManager.getSession().createQuery("SELECT obj FROM Class<?> obj");
//        });
        return List.of();
    }

    @Override
    public T get(long id) {
        return null;
    }

    @Override
    public void create(T object) {

    }

    @Override
    public void update(T object) {

    }

    @Override
    public void delete(long id) {

    }
}
