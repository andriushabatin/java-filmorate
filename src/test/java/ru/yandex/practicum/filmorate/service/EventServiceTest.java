package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.data.EventType;
import ru.yandex.practicum.filmorate.data.Operation;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EventServiceTest {
    @Autowired
    private final UserService userService;
    @Autowired
    private final EventService eventService;

    @Test
    void eventServiceTest() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> eventService.getFeed(2222));
        assertTrue(Objects.requireNonNull(exception.getMessage()).contains("Пользователь не найден!"),
                "При попытке получения ленты событий для пользователя с не верным id получено не верное исключение");

        final User user1 = userService.create(new User(0,
                "user1@mail.ru",
                "user1",
                "big",
                new Date()
        ));
        final User user2 = userService.create(new User(0,
                "user2@mail.ru",
                "user2",
                "small",
                new Date()
        ));
        final NotFoundException exception1 = assertThrows(
                NotFoundException.class,
                () -> eventService.createEvent(2222, EventType.FRIEND, Operation.ADD, user2.getId()));
        assertTrue(Objects.requireNonNull(exception1.getMessage()).contains("Пользователь не найден!"),
                "При попытке получения ленты событий для пользователя с не верным id получено не верное исключение");
        eventService.createEvent(user1.getId(), EventType.FRIEND, Operation.ADD, user2.getId());
        eventService.createEvent(user1.getId(), EventType.FRIEND, Operation.REMOVE, user2.getId());
        eventService.createEvent(user1.getId(), EventType.FRIEND, Operation.ADD, user2.getId());
        final List<Event> feed = eventService.getFeed(user1.getId());
        assertEquals(feed.size(), 3, "неверное количество событий в ленте для пользователя с id = " + user1.getId());
    }
}