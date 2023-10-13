package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Component
@Qualifier("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreStorage filmGenreStorage;
    private final LikeStorage likeStorage;
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
                stmt.setInt(5, film.getRate());
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
                    "SET name=?, description=?, release=?, duration=?, rate=?, rating_id=?\n" +
                    "WHERE film_id=?";

            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration().toMinutes(),
                    film.getRate(),
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
        put(film);
    }

    @Override
    public void deleteLike(int id, User user) {

        Film film = findFilmById(id);
        likeStorage.deleteLike(film, user);
        film.setRate(film.getRate() - 1);
        put(film);
    }

    @Override
    public List<Film> findAllFilmsByDirector(int id, String sortBy) {

        directorStorage.findDirectorById(id);

        List<Integer> filmIds = filmDirectorStorage.findFilmIdsOfDirector(id, sortBy);

        return filmIds.stream()
                .map(this::findFilmById)
                .collect(Collectors.toList());
    }

    public List<Film> searchFilmsBySubstring(String query, List<String> by) {

        List<Film> response = new ArrayList<>();
        String queryAsLowerCase = "%" + query.toLowerCase() + "%";

        if (by.contains("title") && by.contains("director")) {

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

            response = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> (makeFilm(rs)), queryAsLowerCase, queryAsLowerCase);

        } else if (by.contains("title")) {

            String sqlQuery = "SELECT DISTINCT f.*, " +
                    "r.rating, " +
                    "COUNT(l.film_id) " +
                    "FROM film f " +
                    "LEFT JOIN rating r ON f.rating_id = r.rating_id " +
                    "LEFT JOIN likes l ON f.film_id = l.film_id " +
                    "WHERE LOWER(f.name) LIKE ? " +
                    "GROUP BY f.film_id " +
                    "ORDER BY COUNT(l.film_id) DESC ";

            response = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> (makeFilm(rs)), queryAsLowerCase);

        } else if (by.contains("director")) {

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

            response = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> (makeFilm(rs)), queryAsLowerCase);
        }

        return response;
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
