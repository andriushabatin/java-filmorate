package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping({"/users"})
    public User create(@RequestBody User user) throws ValidationException, ObjectAlreadyExistException {
        return userService.create(user);
    }

    @PutMapping({"/users"})
    public User put(@RequestBody User user) throws ValidationException {
        return userService.put(user);
    }

    @GetMapping({"/users"})
    public List<User> findAll() {
        return userService.findAll();
    }
}
