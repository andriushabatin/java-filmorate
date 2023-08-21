package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User create(User user) throws ObjectAlreadyExistException, ValidationException {
        try {
            if (UserValidator.isValid(user)) {
                return userRepository.save(user);
            }
            return user;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public User put(User user) throws ValidationException {
        try {
            if (UserValidator.isValid(user)) {
                Optional<User> userDb = this.userRepository.findById(user.getId());
                if (userDb.isPresent()) {
                    User userUpdate = userDb.get();
                    userUpdate.setId(user.getId());
                    userUpdate.setEmail(user.getEmail());
                    userUpdate.setLogin(user.getLogin());
                    userUpdate.setName(user.getName());
                    userUpdate.setBirthday(user.getBirthday());
                    userRepository.save(userUpdate);
                    return userUpdate;
                } else {
                    throw new NotFoundException("User not found with id : " + user.getId());
                }
            }
            return user;
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        Optional <User> userDb = this.userRepository.findById(id);

        if (userDb.isPresent()) {
            return userDb.get();
        } else {
            throw new NotFoundException("User not found with id : " + id);
        }
    }

    @Override
    public void deleteAll() {
        this.userRepository.deleteAll();
    }
}
