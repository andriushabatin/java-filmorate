package ru.yandex.practicum.filmorate.exception.user;

public class InvalidEmailException extends Exception {
    public InvalidEmailException(String message) {
        super(message);
    }
}
