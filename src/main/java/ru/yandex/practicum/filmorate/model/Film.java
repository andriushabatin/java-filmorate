package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.Genre;
import ru.yandex.practicum.filmorate.Rating;
import ru.yandex.practicum.filmorate.converter.DurationDeserializer;
import ru.yandex.practicum.filmorate.converter.DurationSerializer;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipMapper;


import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "film")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "film_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "releaseDate")
    private LocalDate releaseDate;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    @Column(name = "duration")
    private Duration duration;

    //private final List<Genre> genre;

    @Column(name = "rating")
    private Rating rating;

    //private Set<Integer> likes;

    /*public Film(int id,
                String name,
                String description,
                LocalDate releaseDate,
                Duration duration,
                List<Genre> genre,
                Rating rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
        this.genre = genre;
        this.rating = rating;
    }*/

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return likes.size() == film.likes.size();
    }*/

    /*@Override
    public int hashCode() {
        return Objects.hash(likes.size());
    }*/
}
