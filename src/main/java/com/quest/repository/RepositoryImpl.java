package com.quest.repository;

import com.quest.config.SessionCreator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;

public class RepositoryImpl<T> implements Repository<T> {
    private final Logger log = LogManager.getLogger(RepositoryImpl.class);
    private final SessionCreator sessionCreator;
    private final Class<T> entityClass;

    public RepositoryImpl(SessionCreator sessionCreator, Class<T> entityClass) {
        this.sessionCreator = sessionCreator;
        this.entityClass = entityClass;
    }

    @Override
    public Collection<T> getAll() {
        try (Session session = sessionCreator.getSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(entityClass);
            Root<T> root = query.from(entityClass);
            query.select(root);
            return session.createQuery(query).list();
        } catch (Exception e) {
            String message = "Error getting all entities from repository";
            log.error(message);
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public T get(long id) {
        try (Session session = sessionCreator.getSession()) {
            return session.get(entityClass, id);
        } catch (Exception e) {
            String message = "Error getting entity by id from repository";
            log.error(message);
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public void create(T object) {
        Session session = sessionCreator.getSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            try {
                session.persist(object);
                transaction.commit();
            } catch (HibernateException e) {
                transaction.rollback();
                log.error(e);
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(T object) {
        Session session = sessionCreator.getSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            session.merge(object);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            log.error(e);
            throw new RuntimeException("Error updating entity", e);
        }
    }

    @Override
    public void delete(long id) {
        Session session = sessionCreator.getSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            session.remove(get(id));
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            log.error(e);
            throw new RuntimeException("Error deleting entity", e);
        }
    }
}
