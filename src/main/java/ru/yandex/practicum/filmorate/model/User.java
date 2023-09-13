package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class User {

    private int id;
    private String email;
    private String login;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthday;

    //private Set<Integer> friends;

    public User() {
    }

    public User(String email, String login, Date birthday) {
        this.email = email;
        this.login = login;
        this.name = this.login;
        this.birthday = birthday;
        //this.friends = new HashSet<>();
    }
}


