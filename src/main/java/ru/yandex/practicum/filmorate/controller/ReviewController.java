package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @PutMapping
    public Review replaceReview(@RequestBody Review review) {
        return reviewService.replaceReview(review);
    }

    @GetMapping
    public List<Review> findAllReviews() {
        return reviewService.findAllReviews();
    }

    @GetMapping("/{id}")
    public Review findReviewById(@PathVariable Integer id) {
        return reviewService.findReviewById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteReviewById(@PathVariable Integer id) {
        reviewService.deleteReviewById(id);
    }

    @GetMapping(params = "filmId")
    public List<Review> findReviewsByFilmId(@RequestParam Integer filmId, @RequestParam(required = false,
            defaultValue = "10") Integer count) {
        return reviewService.findReviewsByFilmId(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.putLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void putDislike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.putDislike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.deleteDislike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.deleteLike(id, userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        return Map.of("error:", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        return Map.of("error:", e.getMessage());
    }
}