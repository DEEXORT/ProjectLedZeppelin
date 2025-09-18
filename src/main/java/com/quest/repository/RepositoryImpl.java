package com.quest.repository;

import com.quest.config.SessionCreator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Collection;

public class RepositoryImpl<T> implements Repository<T> {
    private final SessionCreator sessionCreator;
    private final Class<T> entityClass;

    public RepositoryImpl(SessionCreator sessionCreator, Class<T> entityClass) {
        this.sessionCreator = sessionCreator;
        this.entityClass = entityClass;
    }

    @Override
    public Collection<T> getAll() {
        Session session = sessionCreator.getSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            try {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<T> query = builder.createQuery(entityClass);
                Root<T> root = query.from(entityClass);
                query.select(root);
                transaction.commit();
                return session.createQuery(query).list();
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Error getting all entities", e);
            }
        } catch (Exception e) {
            // Если ошибка с соединением
            throw new RuntimeException("Error getting all", e);
        }
    }

    @Override
    public T get(long id) {
        return null;
    }

    @Override
    public void create(T object) {
        Session session = sessionCreator.getSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            session.persist(object);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(T object) {

    }

    @Override
    public void delete(long id) {

    }
}
