package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.config.SessionCreator;
import com.quest.entity.Achievement;
import com.quest.entity.User;
import com.quest.repository.RepositoryImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HibernateAchievementServiceIT extends ContainerIT {
    private static HibernateAchievementService achievementService;

    @BeforeAll
    static void setUp() {
//        sessionFactory = new Configuration()
//                .configure("hibernate-test.cfg.xml")
//                .addAnnotatedClass(Achievement.class)
//                .addAnnotatedClass(User.class)
//                .buildSessionFactory();
//        SessionCreator creator = new SessionCreator(sessionFactory);
//        RepositoryImpl<Achievement> repository = new RepositoryImpl<>(sessionCreator, Achievement.class);
//        achievementService = new HibernateAchievementService(repository);
        achievementService = ServiceLocator.getService(HibernateAchievementService.class);

        Achievement defaultAchievement = Achievement.builder()
                .text("Achievement test")
                .build();
        achievementService.create(defaultAchievement);
    }

    @Test
    void get() {
        // given
        Achievement achievement = Achievement.builder()
                .text("testGet")
                .build();
        achievementService.create(achievement);

        // when
        Optional<Achievement> updated = achievementService.get(achievement.getId());

        // then
        assertTrue(updated.isPresent());
        assertEquals("testGet", updated.get().getText());
    }

    @Test
    void getAll() {
        // given
        Achievement achievement = Achievement.builder()
                .text("testGetAll")
                .build();
        achievementService.create(achievement);

        // when
        Collection<Achievement> achievements = achievementService.getAll();

        // then
        assertFalse(achievements.isEmpty());
    }

    @Test
    void create() {
        // given
        Achievement achievement = Achievement.builder()
                .text("testCreate")
                .build();

        // when
        achievementService.create(achievement);

        // then
        assertNotNull(achievement.getId());
        assertEquals("testCreate", achievement.getText());
    }

    @Test
    void update() {
        // given
        Achievement achievement = Achievement.builder()
                .text("testCreate")
                .build();
        achievementService.create(achievement);

        // when
        achievement.setText("testUpdate");
        achievementService.update(achievement);

        // then
        Optional<Achievement> updated = achievementService.get(achievement.getId());
        assertTrue(updated.isPresent());
        assertEquals("testUpdate", updated.get().getText());
    }

    @Test
    void delete() {
        // given
        Achievement achievement = Achievement.builder()
                .text("testDelete")
                .build();
        achievementService.create(achievement);

        // when
        achievementService.delete(achievement.getId());

        // then
        Optional<Achievement> deleted = achievementService.get(achievement.getId());
        assertFalse(deleted.isPresent());
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}