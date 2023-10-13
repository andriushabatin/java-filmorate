package ru.yandex.practicum.filmorate.dao.review.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.review.ReviewStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.validator.ReviewValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Qualifier("ReviewDbStorage")
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review createReview(Review review) {
        if (ReviewValidator.isValid(review)) {
            String sqlQueryToReviews = "INSERT INTO reviews (film_id, user_id, content, is_positive, useful)" +
                    " VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQueryToReviews, new String[]{"review_id"});
                stmt.setInt(1, review.getFilmId());
                stmt.setInt(2, review.getUserId());
                stmt.setString(3, review.getContent());
                stmt.setBoolean(4, review.getIsPositive());
                stmt.setInt(5, review.getUseful());
                return stmt;
            }, keyHolder);
            return findReviewById(keyHolder.getKey().intValue());
        } else {
            return null;
        }
    }

    @Override
    public List<Review> findAllReviews() {
        String sqlQueryFromReview = "SELECT * FROM reviews ORDER BY useful DESC";
        return jdbcTemplate.query(sqlQueryFromReview, this::makeReview);
    }

    @Override
    public List<Review> findReviewsByFilmId(Integer filmId, Integer count) {
        String sqlQueryFromReview = "SELECT * FROM reviews WHERE film_id = ? ORDER BY useful DESC LIMIT ?";
        return jdbcTemplate.query(sqlQueryFromReview, this::makeReview, filmId, count);
    }

    @Override
    public Review findReviewById(Integer id) {
        try {
            String sqlQueryFromReview = "SELECT * FROM reviews WHERE review_id = ?";
            return jdbcTemplate.queryForObject(sqlQueryFromReview, this::makeReview, id);
        } catch (RuntimeException e) {
            throw new NotFoundException("Отзыва с таким id нет");
        }
    }

    @Override
    public Review replaceReview(Review review) {
        checkAvailabilityReview(review.getReviewId());
        String sqlQueryToReviews = "UPDATE reviews SET content = ?, is_positive = ? WHERE review_id = ?";
        jdbcTemplate.update(sqlQueryToReviews, review.getContent(), review.getIsPositive(), review.getReviewId());
        return findReviewById(review.getReviewId());
    }

    @Override
    public void deleteDislike(Integer id, Integer userId) {
        checkAvailabilityReview(id);
        String sqlQueryToReviews = "UPDATE reviews SET useful = useful + 1 WHERE review_id = ?";
        jdbcTemplate.update(sqlQueryToReviews, id);
    }

    @Override
    public void deleteReviewById(Integer id) {
        checkAvailabilityReview(id);
        String sqlQueryToReviews = "DELETE FROM reviews WHERE review_id = ?";
        jdbcTemplate.update(sqlQueryToReviews, id);
    }

    @Override
    public void deleteLike(Integer id, Integer userId) {
        checkAvailabilityReview(id);
        String sqlQueryToReviews = "UPDATE reviews SET useful = useful - 1 WHERE review_id = ?";
        jdbcTemplate.update(sqlQueryToReviews, id);
    }

    @Override
    public void putDislike(Integer id, Integer userId) {
        checkAvailabilityReview(id);
        String sqlQueryToReviews = "UPDATE reviews SET useful = useful - 1 WHERE review_id = ?";
        jdbcTemplate.update(sqlQueryToReviews, id);
    }

    @Override
    public void putLike(Integer id, Integer userId) {
        checkAvailabilityReview(id);
        String sqlQueryToReviews = "UPDATE reviews SET useful = useful + 1 WHERE review_id = ?";
        jdbcTemplate.update(sqlQueryToReviews, id);

    }

    private void checkAvailabilityReview(Integer id) {
        String sqlQueryFromReviews = "SELECT COUNT(*) FROM reviews WHERE review_id = ?";
        if (jdbcTemplate.queryForObject(sqlQueryFromReviews, Integer.class, id) <= 0) {
            throw new NotFoundException("Отзыва с таким id нет");
        }
    }

    private Review makeReview(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .reviewId(rs.getInt("review_id"))
                .filmId(rs.getInt("film_id"))
                .userId(rs.getInt("user_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .useful(rs.getInt("useful")).build();
    }
}