package com.quest.repository;

import com.quest.config.SessionCreator;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public class RepositoryImpl<T> implements Repository<T> {
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
            log.error(e.getMessage(), e);
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public T get(long id) {
        try (Session session = sessionCreator.getSession()) {
            return session.get(entityClass, id);
        } catch (Exception e) {
            String message = "Error getting entity by id from repository";
            log.error(e.getMessage(), e);
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public void create(T object) {
        Session session = sessionCreator.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(object);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    @Override
    public void update(T object) {
        Session session = sessionCreator.getSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            try {
                session.merge(object);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error updating entity", e);
        }
    }

    @Override
    public void delete(T object) {
        Session session = sessionCreator.getSession();
        session.remove(object);
        Transaction transaction = session.beginTransaction();
        try (session) {
            try {
                session.remove(object);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            transaction.rollback();
            log.error(e.getMessage(), e);
            throw new RuntimeException("Error deleting entity", e);
        }
    }

    public Stream<T> find(T object) {
        Session session = sessionCreator.getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> critQuery = builder.createQuery(entityClass);

        Root<T> root = critQuery.from(entityClass);
        critQuery.select(root);

        Field[] fields = object.getClass().getDeclaredFields();
        List<Predicate> predicates = new ArrayList<>();
        for (Field field : fields) {
            // SELECT * FROM <entityClass> WHERE field.getName() = field.get(object)
            try {
                field.setAccessible(true);
                String name = field.getName();
                Object value = field.get(object);

                if (isPredicate(field, value)) {
                    Predicate predicate = builder.equal(root.get(name), value);
                    predicates.add(predicate);
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        critQuery.where(predicates.toArray(new Predicate[0]));
        Query<T> sessionQuery = session.createQuery(critQuery);
        return sessionQuery.list().stream();
    }

    private boolean isPredicate(Field field, Object value) {
        return Objects.nonNull(value)
                && !field.isAnnotationPresent(Transient.class)
                && !field.isAnnotationPresent(OneToMany.class)
                && !field.isAnnotationPresent(OneToOne.class)
                && !field.isAnnotationPresent(ManyToMany.class)
                && !field.isAnnotationPresent(ManyToOne.class);
    }
}
