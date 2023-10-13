package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.director.DirectorStorage;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.film_director.FilmDirectorStorage;
import ru.yandex.practicum.filmorate.dao.film_genre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.dao.like.LikeStorage;
import ru.yandex.practicum.filmorate.dao.like.impl.LikeDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("FilmDbStorage")
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreStorage filmGenreStorage;
    private final LikeStorage likeStorage;

    private final LikeDbStorage likeDbStorage;
    private final FilmDirectorStorage filmDirectorStorage;

    private final DirectorStorage directorStorage;

    @Override
    public Film create(Film film) throws ObjectAlreadyExistException, ValidationException {

        if (FilmValidator.isValid(film)) {
            String sqlQuery = "insert into film(name, description, release, duration, rate, rating_id) " +
                    "values (?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setObject(3, film.getReleaseDate());
                stmt.setObject(4, film.getDuration().toMinutes());
                stmt.setInt(5, 0);
                stmt.setObject(6, film.getMpa().getId());
                return stmt;
            }, keyHolder);

            List<Genre> genres;
            if (Optional.ofNullable(film.getGenres()).isPresent()) {
                genres = new ArrayList<>(film.getGenres());
            } else {
                genres = new ArrayList<>();
            }
            filmGenreStorage.createFilmGenreRelations(keyHolder.getKey().intValue(), genres);

            List<Director> directors;
            if (Optional.ofNullable(film.getDirectors()).isPresent()) {
                directors = new ArrayList<>(film.getDirectors());
            } else {
                directors = new ArrayList<>();
            }
            filmDirectorStorage.createFilmDirectorRelations(keyHolder.getKey().intValue(), directors);

            return findFilmById(keyHolder.getKey().intValue());
        } else {
            return null;
        }
    }

    @Override
    public Film put(Film film) throws ValidationException {

        if (FilmValidator.isValid(film)) {
            String sqlQuery = "UPDATE film\n" +
                    "SET name=?, description=?, release=?, duration=?, rating_id=?\n" +
                    "WHERE film_id=?";

            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration().toMinutes(),
                    film.getMpa().getId(),
                    film.getId());

            List<Genre> genres;
            if (Optional.ofNullable(film.getGenres()).isPresent()) {
                genres = new ArrayList<>(film.getGenres());
            } else {
                genres = new ArrayList<>();
            }
            filmGenreStorage.updateFilmGenreRelations(film.getId(), genres);

            List<Director> directors;
            if (Optional.ofNullable(film.getDirectors()).isPresent()) {
                directors = new ArrayList<>(film.getDirectors());
            } else {
                directors = new ArrayList<>();
            }
            filmDirectorStorage.updateFilmDirectorRelations(film.getId(), directors);

            return findFilmById(film.getId());
        } else {
            return null;
        }
    }

    @Override
    public List<Film> findAll() {

        String sqlQuery = "SELECT f.film_id,\n" +
                "f.name,\n" +
                "f.description,\n" +
                "f.release,\n" +
                "f.duration,\n" +
                "f.rate,\n" +
                "f.rating_id,\n" +
                "r.rating\n" +
                "FROM film AS f\n" +
                "LEFT JOIN rating AS r ON f.rating_id = r.rating_id\n" +
                "GROUP BY f.FILM_ID\n";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film findFilmById(int id) {

        String sqlQuery = "SELECT f.film_id,\n" +
                "f.name,\n" +
                "f.description,\n" +
                "f.release,\n" +
                "f.duration,\n" +
                "f.rating_id,\n" +
                "f.rate,\n" +
                "r.rating\n" +
                "FROM film AS f\n" +
                "LEFT JOIN rating AS r ON f.rating_id = r.rating_id\n" +
                "GROUP BY f.FILM_ID\n" +
                "HAVING f.film_id = ?";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (filmRows.next()) {
            Film film = new Film();
            film.setId(filmRows.getInt("film_id"));
            film.setName(filmRows.getString("name"));
            film.setDescription(filmRows.getString("description"));
            film.setReleaseDate(filmRows.getDate("release"));
            film.setDuration(Duration.ofMinutes(filmRows.getLong("duration")));
            film.setRate(filmRows.getInt("rate"));
            film.setMpa(new Mpa(filmRows.getInt("rating_id"), filmRows.getString("rating")));
            film.setGenres(new HashSet<>(filmGenreStorage.findGenresByFilmId(id)));
            film.setDirectors(new HashSet<>(filmDirectorStorage.findDirectorByFilmId(id)));
            return film;
        } else {
            throw new NotFoundException("Фильм не найден!");
        }
    }

    @Override
    public void likeFilm(int id, User user) throws ValidationException {

        Film film = findFilmById(id);
        likeStorage.likeFilm(film, user);
        film.setRate(film.getRate() + 1);
        replaceRate(film.getId(), film.getRate());
    }

    @Override
    public void deleteLike(int id, User user) {

        Film film = findFilmById(id);
        likeStorage.deleteLike(film, user);
        film.setRate(film.getRate() - 1);
        replaceRate(film.getId(), film.getRate());
    }

    @Override
    public List<Film> findAllFilmsByDirector(int id, String sortBy) {

        directorStorage.findDirectorById(id);

        List<Integer> filmIds = filmDirectorStorage.findFilmIdsOfDirector(id, sortBy);

        return filmIds.stream()
                .map(this::findFilmById)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFilm(int id) {
        String genreSql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(genreSql, id);
        String reviewSql = "DELETE FROM reviews WHERE film_id = ?";
        jdbcTemplate.update(reviewSql, id);
        String sql = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {

        String sqlQuery = "SELECT f.*, r.rating fr, COUNT(l.user_id) " +
                "FROM film f " +
                "LEFT JOIN likes l ON f.film_id = l.film_id " +
                "JOIN rating r ON r.rating_id = f.rating_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.user_id) DESC, f.name " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), count);
    }

    private void replaceRate(int filmId, int newRating) {
        String sqlQuery = "UPDATE film SET rate = ? WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, newRating, filmId);
    }

    @Override
    public List<Film> findCommonFilms(int userId, int friendId) {

        List<Film> userFilms = likeDbStorage.findFilmsIdsOfUser(userId).stream()
                .map(this::findFilmById)
                .collect(Collectors.toList());

        List<Film> friendFilm = likeDbStorage.findFilmsIdsOfUser(friendId).stream()
                .map(this::findFilmById)
                .collect(Collectors.toList());

        return userFilms.stream()
                .filter(friendFilm::contains)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> searchFilmsByTitleAndDirector(String query) {

        String sqlQuery = "SELECT DISTINCT f.*, " +
                "r.rating, " +
                "COUNT(l.film_id)" +
                "FROM film f " +
                "LEFT JOIN rating r ON f.rating_id = r.rating_id " +
                "LEFT JOIN likes l ON f.film_id = l.film_id " +
                "LEFT JOIN film_director fd ON f.film_id = fd.film_id " +
                "LEFT JOIN directors d ON fd.director_id = d.director_id " +
                "WHERE LOWER(f.name) LIKE ? OR LOWER(d.name) LIKE ? " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.film_id) DESC";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> (makeFilm(rs)), query, query);
    }

    @Override
    public List<Film> searchFilmsByTitle(String query) {
        String sqlQuery = "SELECT DISTINCT f.*, " +
                "r.rating, " +
                "COUNT(l.film_id) " +
                "FROM film f " +
                "LEFT JOIN rating r ON f.rating_id = r.rating_id " +
                "LEFT JOIN likes l ON f.film_id = l.film_id " +
                "WHERE LOWER(f.name) LIKE ? " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.film_id) DESC ";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> (makeFilm(rs)), query);
    }

    @Override
    public List<Film> searchFilmsByDirector(String query) {
        String sqlQuery = "SELECT distinct f.*, " +
                "r.rating, " +
                "COUNT(l.film_id)  " +
                "FROM film f " +
                "LEFT JOIN rating r ON f.rating_id = r.rating_id " +
                "LEFT JOIN likes l ON f.film_id = l.film_id " +
                "LEFT JOIN film_director fd ON f.film_id = fd.film_id " +
                "LEFT JOIN directors d ON d.director_id = fd.director_id " +
                "WHERE LOWER(d.name) LIKE ? " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.film_id) DESC ";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> (makeFilm(rs)), query);
    }

    @Override
    public List<Film> getMostPopularFilmsByYear(Integer count, Integer year) {

        String sqlQuery = "SELECT f.*, r.rating " +
                "FROM film f " +
                "LEFT JOIN likes l ON f.film_id = l.film_id " +
                "JOIN rating r ON r.rating_id = f.rating_id " +
                "JOIN film_genre fg ON f.film_id = fg.film_id " +
                "WHERE YEAR(f.release) = ? " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), year, count);
    }

    @Override
    public List<Film> getMostPopularFilmsByGenre(Integer count, Integer genreId) {

        String sqlQuery = "SELECT f.*, r.rating " +
                "FROM film f " +
                "LEFT JOIN likes l ON f.film_id = l.film_id " +
                "JOIN rating r ON r.rating_id = f.rating_id " +
                "JOIN film_genre fg ON f.film_id = fg.film_id " +
                "WHERE fg.genre_id = ? " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), genreId, count);
    }

    @Override
    public List<Film> getMostPopularFilmsByGenreAndYear(Integer count, Integer genreId, Integer year) {

        String sqlQuery = "SELECT f.*, r.rating, COUNT(l.user_id) " +
                "FROM film f " +
                "LEFT JOIN likes l ON f.film_id = l.film_id " +
                "JOIN rating r ON r.rating_id = f.rating_id " +
                "JOIN film_genre fg ON f.film_id = fg.film_id " +
                "WHERE YEAR(f.release) = ? AND fg.genre_id = ?" +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), year, genreId, count);

    }

    @Override
    public List<Film> getRecommendationsForUser(int userId) {
        String sqlGetMaximumIntersection = "SELECT l.user_id as user_id FROM likes as l " +
                "WHERE l.film_id in (SELECT film_id from likes l1 where user_id = ?) " +
                "AND l.user_id <> ? " +
                "GROUP BY l.user_id " +
                "ORDER BY count(l.film_id);";
        String sqlGetRecommendations = "SELECT f.*, r.RATING FROM film as f " +
                "LEFT JOIN RATING AS r ON f.RATING_ID  = r.RATING_ID WHERE f.film_id in " +
                "(SELECT f1.film_id FROM film AS f1 LEFT JOIN likes AS l1 ON f1.film_id = l1.film_id " +
                "WHERE l1.user_id = ? " +
                "EXCEPT " +
                "SELECT f2.film_id FROM film AS f2 LEFT JOIN likes AS l2 ON f2.film_id = l2.film_id " +
                "WHERE l2.user_id = ?);";
        final List<Integer> userIds = jdbcTemplate.query(sqlGetMaximumIntersection,
                (rs, rowNum) -> rs.getInt("user_id"), userId, userId);
        for (Integer id : userIds) {
            List<Film> recommendedFilms = jdbcTemplate.query(sqlGetRecommendations,
                    (rs, rowNum) -> makeFilm(rs), id, userId);
            if (!recommendedFilms.isEmpty()) {
                return recommendedFilms;
            }
        }
        return new ArrayList<Film>();
    }

    private Film makeFilm(ResultSet rs) throws SQLException {

        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release"));
        film.setDuration(Duration.ofMinutes(rs.getLong("duration")));
        film.setRate(rs.getInt("rate"));
        film.setMpa(new Mpa(rs.getInt("rating_id"), rs.getString("rating")));
        film.setGenres(new HashSet<>(filmGenreStorage.findGenresByFilmId(rs.getInt("film_id"))));
        film.setDirectors(new HashSet<>(filmDirectorStorage.findDirectorByFilmId(rs.getInt("film_id"))));

        return film;
    }
}
