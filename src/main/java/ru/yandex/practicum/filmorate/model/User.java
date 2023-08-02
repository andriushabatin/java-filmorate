package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private int id;
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;

    private Set<Integer> friends;

    public User(String email, String login, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = this.login;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }
}


