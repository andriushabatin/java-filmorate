package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public User create(User user) throws ObjectAlreadyExistException, ValidationException;

    public User put(User user) throws ValidationException;

    public List<User> findAll();

    public User getUserById(int id);
}
