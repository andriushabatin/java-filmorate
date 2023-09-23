package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Friendship {

    private int user_id;
    private int friend_id;
    private int status;
}
