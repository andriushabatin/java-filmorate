package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "genre")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {

    @Id
    @Column(name = "genre_id")
    private int id;

    @Column(name = "genre")
    String genre;
}
