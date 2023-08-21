package ru.yandex.practicum.filmorate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmRepository extends JpaRepository<Film, Integer> {
}
