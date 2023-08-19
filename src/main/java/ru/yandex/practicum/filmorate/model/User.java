package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_table")
@Data
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;
    @Column(name = "email")
    private String email;
    @Column(name = "login")
    private String login;
    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
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


