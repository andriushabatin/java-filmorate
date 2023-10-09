package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;

public class ReviewValidator {
    public static boolean isValid(Review review) throws ValidationException {
        if (review.getUserId() < 0) {
            throw new NotFoundException("userId не должен быть отрицательным");
        } else if (review.getFilmId() < 0) {
            throw new NotFoundException("filmId не должен быть отрицательным");
        } else if (review.getUserId() == 0) {
            throw new ValidationException("userId должен быть положительным");
        } else if (review.getFilmId() == 0) {
            throw new ValidationException("filmId должен быть положительным");
        } else if (review.getContent() == null || review.getContent().isEmpty()) {
            throw new ValidationException("Поле content пустое");
        } else if (review.getIsPositive() == null) {
            throw new ValidationException("Поле isPositive пустое");
        }
        return true;
    }
}
