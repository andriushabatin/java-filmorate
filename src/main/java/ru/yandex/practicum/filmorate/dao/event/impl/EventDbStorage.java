package ru.yandex.practicum.filmorate.dao.event.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.event.EventStorage;
import ru.yandex.practicum.filmorate.data.EventType;
import ru.yandex.practicum.filmorate.data.Operation;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Qualifier("EventDbStorage")
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createEvent(Event event) {
        final String sql = "INSERT INTO feed (user_id, timestamp, type_id, operation_id, entity_id) VALUES (?, ?, ?, ?, ?);";
        jdbcTemplate.update(
                sql,
                event.getUserId(),
                event.getTimestamp(),
                event.getEventType().getId(),
                event.getOperation().getId(),
                event.getEntityId()
        );
    }

    @Override
    public List<Event> getFeed(int userId) {
        final String sql = "SELECT * FROM feed WHERE user_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeEvent(rs), userId);
    }

    private Event makeEvent(ResultSet rs) throws SQLException {
        return Event.builder()
                .eventId(rs.getInt("id"))
                .timestamp(rs.getLong("timestamp"))
                .userId(rs.getInt("user_id"))
                .eventType(EventType.getEventType(rs.getInt("type_id")))
                .operation(Operation.getOperation(rs.getInt("operation_id")))
                .entityId(rs.getInt("entity_id"))
                .build();
    }
}
