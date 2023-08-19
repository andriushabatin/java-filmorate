package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();


    private int nextId = 1;

    public User create(User user) throws ObjectAlreadyExistException, ValidationException {
        if (users.containsKey(user.getId())) {
            throw new ObjectAlreadyExistException("Такой пользователь уже существует.");
        } else {
            try {
                if (UserValidator.isValid(user)) {
                    user.setId(getNextId());
                    users.put(user.getId(), user);
                    log.debug("Пользователь " + user.getLogin() + " добавлен.");
                }
                return user;
            } catch (ValidationException e) {
                log.error(e.getMessage());
                throw new ValidationException(e.getMessage());
            }
        }
    }

    public User put(User user) throws ValidationException {
        try {
            if (UserValidator.isValid(user)) {
                log.debug("Пользователь " + users.get(user.getId()).getLogin() + " обновлен.");
                this.users.put(user.getId(), user);
            }
            return user;
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }

    public List<User> findAll() {
        return new ArrayList<>(this.users.values());
    }

    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException();
        }
    }

    public void deleteAll() {
        users.clear();
    }

    public int getNextId() {
        return nextId++;
    }



}
