package com.quest.services.hibernate;

import com.quest.entity.Action;
import com.quest.entity.QuestScene;
import com.quest.repository.RepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HibernateQuestServiceIT extends ContainerIT {
    private HibernateQuestService hibernateQuestService;

    @BeforeEach
    void setUp() {
        hibernateQuestService = new HibernateQuestService(new RepositoryImpl<>(sessionCreator, QuestScene.class),
                new RepositoryImpl<>(sessionCreator, Action.class));
    }

    @AfterEach
    void tearDown() {
        hibernateQuestService.getAll().forEach(hibernateQuestService::delete);
    }

    @Test
    void saveAllScenes() {

    }
}