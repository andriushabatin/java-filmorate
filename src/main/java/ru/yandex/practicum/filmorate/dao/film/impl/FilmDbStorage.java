package ru.yandex.practicum.filmorate.dao.film.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.storage.film.FilmMapper;

import java.sql.PreparedStatement;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) throws ObjectAlreadyExistException, ValidationException {

        //return filmRepository.save(film);

        String sqlQuery = "insert into film(name, description, release, duration, rating_id) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setObject(3, film.getReleaseDate());
            stmt.setObject(4, film.getDuration().toMinutes());
            stmt.setObject(5, film.getRating().getId());
            return stmt;
        }, keyHolder);

        return findFilmById(keyHolder.getKey().intValue());
    }

    @Override
    public Film put(Film film) throws ValidationException {

        /*Optional<Film> filmDb = this.filmRepository.findById(film.getId());
        if (filmDb.isPresent()) {
            Film filmUpdate = filmDb.get();
            filmUpdate.setId(film.getId());
            filmUpdate.setName(film.getName());
            filmUpdate.setDescription(film.getDescription());
            filmUpdate.setReleaseDate(film.getReleaseDate());
            filmUpdate.setDuration(film.getDuration());
            filmUpdate.setRating(film.getRating());
            filmRepository.save(filmUpdate);
            return filmUpdate;
        } else {
            throw new NotFoundException("Film not found with id : " + film.getId());
        }*/

        /*Optional<Film> filmDb = this.filmRepository.findById(film.getId());
        if (filmDb.isPresent()) {
            Film filmUpdate = filmDb.get();
            filmUpdate.setId(film.getId());
            filmUpdate.getReleaseDate(film.getReleaseDate());
            userRepository.save(filmUpdate);
            return filmUpdate;
        } else {
            throw new NotFoundException("User not found with id : " + user.getId());
        }*/

        return null;
    }

    @Override
    public List<Film> findAll() {
        //return this.filmRepository.findAll();

        String sqlQuery = "";

        return null;
    }

    @Override
    public Film findFilmById(int id) {

        String sqlQuery = "SELECT f.film_id,\n" +
                "f.name,\n" +
                "f.description,\n" +
                "f.release,\n" +
                "f.duration,\n" +
                "f.rating_id,\n" +
                "r.rating\n" +
        "FROM film AS f\n" +
        "LEFT JOIN rating AS r ON f.rating_id = r.rating_id\n" +
        "GROUP BY f.FILM_ID\n" +
        "HAVING f.film_id = ?";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        filmRows.next();

        Film film = new Film();
        film.setId(filmRows.getInt("film_id"));
        film.setName(filmRows.getString("name"));
        film.setDescription(filmRows.getString("description"));
        film.setReleaseDate(filmRows.getDate("release"));
        film.setDuration(Duration.ofMinutes(filmRows.getLong("duration")));
        film.setRating(new Mpa(filmRows.getInt("rating_id"), filmRows.getString("rating")));

        /*Optional <Film> filmDb = this.filmRepository.findById(id);
        if (filmDb.isPresent()) {
            return filmDb.get();
        } else {
            throw new NotFoundException("Film not found with id : " + id);
        }*/

        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {

        String sql = "SELECT *\n" +
                "FROM film AS a\n" +
                "LEFT OUTER JOIN\n" +
                "  (SELECT film_id,\n" +
                "          count(*) AS COUNT\n" +
                "   FROM likes\n" +
                "   GROUP BY film_id) AS b ON b.film_id = a.film_id\n" +
                "ORDER BY COUNT DESC\n" +
                "LIMIT ?;";

        return jdbcTemplate.query(sql,
                new FilmMapper(),
                count);


    }
}
