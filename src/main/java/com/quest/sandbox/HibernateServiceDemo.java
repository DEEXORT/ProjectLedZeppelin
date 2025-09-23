package com.quest.sandbox;

import com.quest.config.ServiceLocator;
import com.quest.entity.User;
import com.quest.entity.character.Player;
import com.quest.services.hibernate.HibernatePlayerService;
import com.quest.services.hibernate.HibernateUserService;

public class HibernateServiceDemo {
    public static void main(String[] args) {
        createUser();
        createPlayer();
    }

    public static void createUser() {
        HibernateUserService service = ServiceLocator.getService(HibernateUserService.class);

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

    public static void createPlayer() {
        HibernatePlayerService service = ServiceLocator.getService(HibernatePlayerService.class);
        System.out.println("============Before query==========");
        for (Player player : service.getAll()) {
            System.out.println("Health = " + player.getHealth());
        }
        System.out.println("==================================");

        Player newPlayer = Player.builder().name("player").level(1).attack(100).health(100).maxHealth(100).userId(1L).build();
        service.create(newPlayer);

        System.out.println("============After query==========");
        for (Player user : service.getAll()) {
            System.out.println("Health = " + user.getHealth());
        }
        System.out.println("==================================");
    }
}
