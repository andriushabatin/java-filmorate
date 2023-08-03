package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.yandex.practicum.filmorate.converter.DurationDeserializer;
import ru.yandex.practicum.filmorate.converter.DurationSerializer;


import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class Film {
    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private final Duration duration;
    private Set<Integer> likes;

    public Film(int id, String name, String description, LocalDate releaseDate, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return likes.size() == film.likes.size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(likes.size());
    }
}
