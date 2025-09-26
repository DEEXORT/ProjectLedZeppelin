package com.quest.services.hibernate;

import com.quest.config.SessionCreator;
import com.quest.entity.Achievement;
import com.quest.entity.User;
import com.quest.repository.RepositoryImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HibernateUserServiceIT {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13-alpine")
                .withDatabaseName("database")
                .withUsername("test")
                .withPassword("test");

    private static SessionFactory sessionFactory;
    private static HibernateUserService hibernateUserService;

    @BeforeAll
    static void setUp() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Achievement.class)
                .buildSessionFactory();
        SessionCreator creator = new SessionCreator(sessionFactory);
        RepositoryImpl<User> repository = new RepositoryImpl<>(creator, User.class);
        hibernateUserService = new HibernateUserService(repository);
    }


    @Test
    void create() {
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
    void getAll() {
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
    void get() {
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
    void update() {
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
    void delete() {
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