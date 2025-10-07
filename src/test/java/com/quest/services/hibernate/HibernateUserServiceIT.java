package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.config.SessionCreator;
import com.quest.entity.Achievement;
import com.quest.entity.User;
import com.quest.repository.RepositoryImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.Optional;

class HibernateUserServiceIT extends ContainerIT {

    private HibernateUserService hibernateUserService;

    @BeforeEach
    void setUp() {
        RepositoryImpl<User> repository = new RepositoryImpl<>(sessionCreator, User.class);
        hibernateUserService = new HibernateUserService(repository);
    }

    @Test
    void shouldCreateUserWithoutAchievement() {
        // given
        User user = User.builder()
                .login("testAdmin")
                .password("testAdmin")
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
                .build();
        User user2 = User.builder()
                .login("testGetAll2")
                .password("testGetAll2")
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
    @Transactional
    void shouldDeleteUserById() {
        // given
        User user = User.builder()
                .login("testDelete")
                .password("testDelete")
                .build();
        hibernateUserService.create(user);

        // when
        hibernateUserService.delete(user);

        // then
        Assertions.assertTrue(hibernateUserService.get(user.getId()).isEmpty());
    }
}