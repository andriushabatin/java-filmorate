package ru.yandex.practicum.filmorate.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LikePrimaryKey implements Serializable {

    private int filmId;
    private int userId;
}
