package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_table")
@Data
public class User {

    @Id
    @Column(name = "user_id")
    private int id;
    @Column(name = "email")
    private String email;
    @Column(name = "login")
    private String login;
    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private LocalDate birthday;

    //private Set<Integer> friends;
    public User() {
    }

    public User(String email, String login, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = this.login;
        this.birthday = birthday;
        //this.friends = new HashSet<>();
    }
}


