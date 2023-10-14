package ru.yandex.practicum.filmorate.dao.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review createReview(Review review);

    Review replaceReview(Review review);

    List<Review> findAllReviews();

    Review findReviewById(Integer id);

    void deleteReviewById(Integer id);

    List<Review> findReviewsByFilmId(Integer filmId, Integer count);

    void putLike(Integer id, Integer userId);

    void putDislike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);

    void deleteDislike(Integer id, Integer userId);
}
