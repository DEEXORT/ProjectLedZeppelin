package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.config.SessionCreator;
import com.quest.entity.Achievement;
import com.quest.entity.User;
import com.quest.repository.RepositoryImpl;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;
import java.util.Optional;

class HibernateUserServiceIT extends ContainerIT {

    static HibernateUserService hibernateUserService;

    //    @Container
//    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13-alpine")
//                .withDatabaseName("database")
//                .withUsername("test")
//                .withPassword("test");
//
//    private static SessionFactory sessionFactory;
//    private static HibernateUserService hibernateUserService;
//
    @BeforeAll
    static void setUp() {
//        sessionFactory = new Configuration()
//                .configure("hibernate-test.cfg.xml")
//                .addAnnotatedClass(User.class)
//                .addAnnotatedClass(Achievement.class)
//                .buildSessionFactory();
//        SessionCreator creator = new SessionCreator(sessionFactory);
//        RepositoryImpl<User> repository = new RepositoryImpl<>(creator, User.class);
//        hibernateUserService = new HibernateUserService(repository);
        hibernateUserService = ServiceLocator.getService(HibernateUserService.class);
    }

    @Test
    void shouldCreateUserWithoutAchievement() {
        // given
        User user = User.builder()
                .login("testAdmin")
                .password("testAdmin")
                .playerId(1L)
                .build();

        // when
        hibernateUserService.create(user);

        // then
        Assertions.assertNotNull(user.getId());
    }

    @Test
    void shouldCreateUserWithAchievement() {
        // given
        Achievement achievement = Achievement.builder()
                .text("test achievement")
                .build();
        User user = User.builder()
                .login("testCreateUserWithAchievement")
                .password("testCreateUserWithAchievement")
                .playerId(1L)
                .build();
        user.getAchievements().add(achievement);

        // when
        hibernateUserService.create(user);

        // then
        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(achievement.getId());
    }

    @Test
    void shouldGetAllUsers() {
        // given
        User user1 = User.builder()
                .login("testGetAll1")
                .password("testGetAll1")
                .playerId(1L)
                .build();
        User user2 = User.builder()
                .login("testGetAll2")
                .password("testGetAll2")
                .playerId(2L)
                .build();
        hibernateUserService.create(user1);
        hibernateUserService.create(user2);

        // when
        Collection<User> users = hibernateUserService.getAll();

        // then
        Assertions.assertNotNull(users);
    }

    @Test
    void shouldGetUserById() {
        // given
        User user = User.builder()
                .login("testGet")
                .password("testGet")
                .playerId(1L)
                .build();
        hibernateUserService.create(user);

        // when
        Optional<User> userFromDB = hibernateUserService.get(user.getId());

        // then
        Assertions.assertTrue(userFromDB.isPresent());
    }

    @Test
    void shouldUpdateUser() {
        // given
        User user = User.builder()
                .login("testUpdate")
                .password("testUpdate")
                .playerId(1L)
                .build();
        String newLogin = "newLogin";
        hibernateUserService.create(user);
        user.setLogin(newLogin);

        // when
        hibernateUserService.update(user);

        // then
        Optional<User> userFromDB = hibernateUserService.get(user.getId());
        Assertions.assertTrue(userFromDB.isPresent());
        Assertions.assertEquals(newLogin, userFromDB.get().getLogin());
    }

    @Test
    void shouldDeleteUserById() {
        // given
        User user = User.builder()
                .login("testDelete")
                .password("testDelete")
                .playerId(1L)
                .build();
        hibernateUserService.create(user);

        // when
        hibernateUserService.delete(user.getId());

        // then
        Assertions.assertTrue(hibernateUserService.get(user.getId()).isEmpty());
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}