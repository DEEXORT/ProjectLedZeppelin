package com.quest.services.hibernate;

import com.quest.entity.Achievement;
import com.quest.repository.RepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HibernateAchievementServiceIT extends ContainerIT {
    private HibernateAchievementService achievementService;


    @BeforeEach
    void setUp() {
        achievementService = new HibernateAchievementService(new RepositoryImpl<>(sessionCreator, Achievement.class));
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
        achievementService.delete(achievement);

        // then
        Optional<Achievement> deleted = achievementService.get(achievement.getId());
        assertFalse(deleted.isPresent());
    }
}