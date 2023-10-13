package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.event.EventStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.data.EventType;
import ru.yandex.practicum.filmorate.data.Operation;
import ru.yandex.practicum.filmorate.model.Event;

import java.time.Instant;
import java.util.List;


@RequiredArgsConstructor
@Service
public class EventService {

    private final EventStorage eventStorage;

    private final UserStorage userStorage;

    /**
     * Метод возвращает список событий для пользователя с заданным идентификатором
     *
     * @param userId - идентификатор пользователя
     * @return - список событий List<Event>
     */
    public List<Event> getFeed(int userId) {
        userStorage.findUserById(userId);
        return eventStorage.getFeed(userId);
    }

    /**
     * Создает запись о событиях связанных с действиями пользователя с заданным идентификатором
     *
     * @param userId    идентификатор пользователя
     * @param eventType тип события
     * @param operation тип операции который совершил пользователь
     * @param entityId  идентификатор сущности над которой пользователь совершал действия
     */
    public void createEvent(int userId, EventType eventType, Operation operation, int entityId) {
        userStorage.findUserById(userId);
        Event event = Event.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();
        eventStorage.createEvent(event);
    }
}
