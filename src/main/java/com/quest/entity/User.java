package com.quest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String login;
    @Column(nullable = false)
    private String password;
    @Column(name = "character_id")
    private Long playerId; // TODO: rename to character_id

//    @Builder.Default
//    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(name = "user_achievements",
//    joinColumns = @JoinColumn(name = "user_id"),
//    inverseJoinColumns = @JoinColumn(name = "achievement_id"))
//    private List<Achievement> achievements = new ArrayList<>();
//
//    public void addAchievement(Achievement achievement) {
//        this.achievements.add(achievement);
//        achievement.getUsers().add(this);
//    }
//
//    public void removeAchievement(Achievement achievement) {
//        this.achievements.remove(achievement);
//        achievement.getUsers().remove(this);
//    }
}
