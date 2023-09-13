package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.key.FriendshipPrimaryKey;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Friendship {

    private int user_id;

    private int friend_id;

    private int status;
}
