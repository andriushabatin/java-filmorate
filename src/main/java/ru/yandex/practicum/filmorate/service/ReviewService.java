package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.review.ReviewStorage;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;

    public Review createReview(Review review) {
        return reviewStorage.createReview(review);
    }

    public Review replaceReview(Review review) {
        return reviewStorage.replaceReview(review);
    }

    public Review findReviewById(Integer id) {
        return reviewStorage.findReviewById(id);
    }

    public List<Review> findAllReviews() {
        return reviewStorage.findAllReviews();
    }

    public void deleteReviewById(Integer id) {
        reviewStorage.deleteReviewById(id);
    }

    public List<Review> findReviewsByFilmId(Integer filmId, Integer count) {
        return reviewStorage.findReviewsByFilmId(filmId, count);
    }

    public void putLike(Integer id, Integer userId) {
        reviewStorage.putLike(id, userId);
    }

    public void putDislike(Integer id, Integer userId) {
        reviewStorage.putDislike(id, userId);
    }

    public void deleteDislike(Integer id, Integer userId) {
        reviewStorage.deleteDislike(id, userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        reviewStorage.deleteLike(id, userId);
    }
}