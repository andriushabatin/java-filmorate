package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.film.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.user.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;


import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	@Autowired
	private final UserDbStorage userStorage;

	@Autowired
	private final FilmDbStorage filmStorage;

	@Test
	public void testCreateUser() {

		User user = new User(0,
				"mail@mail.ru",
				"dolore",
				"Nick Name",
				new Date()
		);

		Optional<User> userOptional = Optional.ofNullable(userStorage.create(user));
		assertThat(userOptional).isPresent();
	}


	@Test
	public void testFindUserById() {

		Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(1));
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void testCreateFilm() {

		Mpa mpa = new Mpa();
		mpa.setId(1);

		Film film = new Film(1,
				"name",
				"desc",
				new Date(),
				Duration.ofMinutes(100),
				0,
				mpa,
				new HashSet<>()
		);

		Optional<Film> filmOptional = Optional.ofNullable(filmStorage.create(film));
		assertThat(filmOptional).isPresent();
	}
}
