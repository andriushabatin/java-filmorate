package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Friendship {

    private int userId;
    private int friendId;
    private int status;
}
