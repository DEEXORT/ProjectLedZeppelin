package com.quest.util;

import com.quest.config.SessionCreator;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransactionException;

public class TransactionManager {
    private Logger logger;
    private final SessionFactory sessionFactory;
    private static final ThreadLocal<Session> currentSession = new ThreadLocal<>();

    public TransactionManager(SessionCreator sessionCreator) {
        this.sessionFactory = sessionCreator.getSessionFactory();
    }

    public Session getSession() {
        Session session = currentSession.get();
        if (session == null || !session.isOpen()) {
            Session openSession = sessionFactory.openSession();
            currentSession.set(openSession);
            logger.info("Session created for thread {}", Thread.currentThread().getName());
        }
        return session;
    }

    public <T> T doInTransaction(TransactionAction<T> action) {
        Session session = getSession();
        session.beginTransaction();
        try {
            T result = action.execute();
            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(e);
            throw new TransactionException("Failed to execute transaction", e);
        } finally {
            session.close();
            currentSession.remove();
        }
    }

    @FunctionalInterface
    public interface TransactionAction<T> {
        T execute();
    }
}
