package com.quest.config;

import com.quest.entity.Achievement;
import com.quest.entity.User;
import com.quest.entity.character.Character;
import com.quest.entity.character.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionCreator implements AutoCloseable{
    private static SessionFactory sessionFactory;

    public SessionCreator() {
        sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Achievement.class)
                .addAnnotatedClass(Player.class)
                .addAnnotatedClass(Character.class)
                .buildSessionFactory();
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public void close() {
        sessionFactory.close();
    }
}
