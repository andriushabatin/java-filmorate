package ru.yandex.practicum.filmorate.dao.film.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.film.genre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FilmGenreStorage filmGenreStorage;

    @Override
    public Film create(Film film) throws ObjectAlreadyExistException, ValidationException {

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

        filmGenreStorage.createFilmGenreRelations(keyHolder.getKey().intValue(), film.getGenres());

        return findFilmById(keyHolder.getKey().intValue());
        //return filmRepository.save(film);
    }

    @Override
    public Film put(Film film) throws ValidationException {

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

        return findFilmById(film.getId());
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
        //return this.filmRepository.findAll();
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
        film.setGenres(filmGenreStorage.findGenresByFilmId(rs.getInt("film_id")));

        return film;
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
        film.setMpa(new Mpa(filmRows.getInt("rating_id"), filmRows.getString("rating")));
        film.setGenres(filmGenreStorage.findGenresByFilmId(id));

        return film;
        /*Optional <Film> filmDb = this.filmRepository.findById(id);
        if (filmDb.isPresent()) {
            return filmDb.get();
        } else {
            throw new NotFoundException("Film not found with id : " + id);
        }*/
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
