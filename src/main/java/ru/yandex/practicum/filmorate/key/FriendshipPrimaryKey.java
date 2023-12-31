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
public class FriendshipPrimaryKey implements Serializable {

    private int userId;
    private int friendId;
}
