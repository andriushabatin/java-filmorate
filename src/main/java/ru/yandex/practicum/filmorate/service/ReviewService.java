package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.review.ReviewStorage;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.data.EventType;
import ru.yandex.practicum.filmorate.data.Operation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final EventService eventService;

    public Review createReview(Review review) {
        Review createdReview = reviewStorage.createReview(review);
        eventService.createEvent(createdReview.getUserId(),
                EventType.REVIEW, Operation.ADD, createdReview.getReviewId());
        return createdReview;
    }

    public Review replaceReview(Review review) {
        Review replacedReview = reviewStorage.replaceReview(review);
        eventService.createEvent(replacedReview.getUserId(),
                EventType.REVIEW, Operation.UPDATE, replacedReview.getReviewId());
        return replacedReview;
    }

    public Review findReviewById(Integer id) {
        return reviewStorage.findReviewById(id);
    }

    public List<Review> findAllReviews() {
        return reviewStorage.findAllReviews();
    }

    public void deleteReviewById(Integer id) {
        Review deletedReview = findReviewById(id);
        reviewStorage.deleteReviewById(id);
        eventService.createEvent(deletedReview.getUserId(),
                EventType.REVIEW, Operation.REMOVE, deletedReview.getReviewId());
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