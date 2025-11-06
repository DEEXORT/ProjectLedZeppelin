package com.quest.services.hibernate;

import com.quest.dto.UserTo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

class HibernateUserServiceIT extends ContainerIT {

    private HibernateUserService hibernateUserService;

    @BeforeEach
    void setUp() {
        hibernateUserService = new HibernateUserService();
    }

    @Test
    void shouldCreateUserWithoutAchievement() {
        // given
        UserTo user = UserTo.builder()
                .login("testAdmin")
                .password("testAdmin")
                .build();

        // when
        hibernateUserService.create(user);

        // then
        Assertions.assertNotNull(user.getId());
    }

    @Test
    void shouldGetAllUsers() {
        // given
        UserTo user1 = UserTo.builder()
                .login("testGetAll1")
                .password("testGetAll1")
                .build();
        UserTo user2 = UserTo.builder()
                .login("testGetAll2")
                .password("testGetAll2")
                .build();
        hibernateUserService.create(user1);
        hibernateUserService.create(user2);

        // when
        Collection<UserTo> users = hibernateUserService.getAll();

        // then
        Assertions.assertNotNull(users);
    }

    @Test
    void shouldGetUserById() {
        // given
        UserTo user = UserTo.builder()
                .login("testGet")
                .password("testGet")
                .build();
        hibernateUserService.create(user);

        // when
        UserTo userFromDB = hibernateUserService.get(user.getId());

        // then
        Assertions.assertNotNull(userFromDB);
    }

    @Test
    void shouldUpdateUser() {
        // given
        UserTo user = UserTo.builder()
                .login("testUpdate")
                .password("testUpdate")
                .build();
        String newLogin = "newLogin";
        hibernateUserService.create(user);
        user.setLogin(newLogin);

        // when
        hibernateUserService.update(user);

        // then
        UserTo userFromDB = hibernateUserService.get(user.getId());
        Assertions.assertNotNull(userFromDB);
        Assertions.assertEquals(newLogin, userFromDB.getLogin());
    }

    @Test
    @Transactional
    void shouldDeleteUserById() {
        // given
        UserTo user = UserTo.builder()
                .login("testDelete")
                .password("testDelete")
                .build();
        hibernateUserService.create(user);

        // when
        hibernateUserService.delete(user);

        // then
    }

    @Test
    void shouldFindUserByLogin() {
        // given
        UserTo user = UserTo.builder()
                .login("testFindUserByLogin")
                .password("testFindUserByLogin")
                .build();
        hibernateUserService.create(user);

        // when
        Optional<UserTo> userDB = hibernateUserService.get("testFindUserByLogin", "testFindUserByLogin");

        // then
        Assertions.assertTrue(userDB.isPresent());
    }
}