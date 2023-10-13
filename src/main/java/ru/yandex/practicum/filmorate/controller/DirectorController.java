package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping
    public Director create(@RequestBody Director director) throws ValidationException, ObjectAlreadyExistException {
        return directorService.create(director);
    }

    @GetMapping("/{id}")
    public Director findDirectorById(@PathVariable int id) {
        return directorService.findDirectorById(id);
    }

    @GetMapping
    public List<Director> findAll() {
        return directorService.findAll();
    }

    @PutMapping
    public Director put(@RequestBody Director director) throws ValidationException {
        return directorService.put(director);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        directorService.delete(id);
    }
}