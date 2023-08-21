package ru.yandex.practicum.filmorate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.filmorate.key.LikePrimaryKey;
import ru.yandex.practicum.filmorate.model.Film;

public interface LikeRepository extends JpaRepository<Film, LikePrimaryKey> {
}
