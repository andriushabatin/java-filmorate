package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.user.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exception.user.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.user.InvalidLoginException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.HashMap;

@Service
@Slf4j
public class UserService {
    private final HashMap<Integer, User> users = new HashMap<>();

    public User create(User user) throws ObjectAlreadyExistException, ValidationException {
        if (users.containsKey(user.getId())) {
            throw new ObjectAlreadyExistException("Такой пользователь уже существует.");
        } else {
            try {
                if (UserValidator.isValid(user)) {
                    if (user.getName().isEmpty()) {
                        user.setName(user.getLogin());
                    }
                    users.put(user.getId(), user);
                    log.debug("Пользователь " + user.getLogin() + " добавлен.");
                }
                return user;
            } catch (InvalidEmailException | InvalidLoginException | InvalidBirthdayException e) {
                log.error(e.getMessage());
                throw new ValidationException(e.getMessage(), e);
            }
        }
    }

    public User put(User user) throws ValidationException {
        try {
            if (UserValidator.isValid(user)) {
                if (user.getName().isEmpty()) {
                    user.setName(user.getLogin());
                }
                log.debug("Пользователь " + users.get(user.getId()).getLogin() + " обновлен.");
                this.users.put(user.getId(), user);
            }
            return user;
        } catch (InvalidEmailException | InvalidBirthdayException | InvalidLoginException e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage(), e);
        }
    }

    public HashMap<Integer, User> findAll() {
        return this.users;
    }
}
