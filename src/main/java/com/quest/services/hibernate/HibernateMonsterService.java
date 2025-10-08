package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.character.Monster;
import com.quest.repository.Repository;
import com.quest.repository.RepositoryImpl;

public class HibernateMonsterService extends AbstractBaseService<Monster> {
    public HibernateMonsterService() {
        super(ServiceLocator.getService(RepositoryImpl.class, Monster.class));
    }

    public HibernateMonsterService(Repository<Monster> repository) {
        super(repository);
    }
}
