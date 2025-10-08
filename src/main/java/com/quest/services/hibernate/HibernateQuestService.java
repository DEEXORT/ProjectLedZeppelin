package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.Action;
import com.quest.entity.QuestScene;
import com.quest.repository.Repository;
import com.quest.repository.RepositoryImpl;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;

public class HibernateQuestService extends AbstractBaseService<QuestScene> {
    private RepositoryImpl<Action> actionRepository;

    public HibernateQuestService() {
        super(ServiceLocator.getService(RepositoryImpl.class, QuestScene.class));
        actionRepository = ServiceLocator.getService(RepositoryImpl.class, Action.class);
    }

    public HibernateQuestService(Repository<QuestScene> repository, RepositoryImpl<Action> actionRepository) {
        super(repository);
        this.actionRepository = actionRepository;
    }

    @Transactional
    public void saveAllScenes(Map<Long, QuestScene> scenes) {
        Map<Long, Long> idMapping = new HashMap<>();
        for (QuestScene scene : scenes.values()) {
            repository.create(scene);
            idMapping.put(scene.getFileId(), scene.getId());
        }
        updateActions(scenes, idMapping);
    }

    private void updateActions(Map<Long, QuestScene> scenes, Map<Long, Long> idMapping) {
        for (QuestScene scene : scenes.values()) {
            for (Action action : scene.getActions()) {
                // Replace fileId to generated ID by database
                action.setNextQuestSceneId(idMapping.get(action.getNextQuestSceneId()));
                actionRepository.update(action);
            }
        }
    }
}
