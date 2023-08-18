package ru.yandex.practicum.filmorate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.filmorate.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
