package ru.yandex.practicum.filmorate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.filmorate.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
}
