package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.FriendshipStatuses;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipStatus {

    int status_id;

    FriendshipStatuses status;
}
