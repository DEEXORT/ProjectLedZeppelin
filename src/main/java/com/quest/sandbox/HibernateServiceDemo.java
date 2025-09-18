package com.quest.sandbox;

import com.quest.config.ServiceLocator;
import com.quest.entity.User;
import com.quest.services.TestUserService;

public class HibernateServiceDemo {
    public static void main(String[] args) {
        TestUserService service = ServiceLocator.getService(TestUserService.class);

        for (User user : service.getAll()) {
            System.out.println("============Before query==========");
            System.out.println(user.getLogin());
        }
        System.out.println("==================================");

        User newUser = User.builder().login("admin1").password("admin777").build();
        service.create(newUser);

        for (User user : service.getAll()) {
            System.out.println("============After query==========");
            System.out.println(user.getLogin());
        }
        System.out.println("==================================");
    }
}
