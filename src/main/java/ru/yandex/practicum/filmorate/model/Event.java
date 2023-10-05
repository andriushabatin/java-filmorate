package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.data.EventType;
import ru.yandex.practicum.filmorate.data.Operation;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    int eventId;
    long timestamp;
    int userId;
    EventType eventType;
    Operation operation;
    int entityId;
}
