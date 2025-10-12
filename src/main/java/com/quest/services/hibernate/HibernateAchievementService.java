package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.Achievement;
import com.quest.repository.RepositoryImpl;

public class HibernateAchievementService extends AbstractBaseService<Achievement> {

    public HibernateAchievementService() {
        super(ServiceLocator.getService(RepositoryImpl.class, Achievement.class));
    }

    public HibernateAchievementService(RepositoryImpl<Achievement> repository) {
        super(repository);
    }
}
