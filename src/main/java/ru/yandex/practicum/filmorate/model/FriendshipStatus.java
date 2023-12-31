package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.data.FriendshipStatuses;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipStatus {

    private int statusId;
    private FriendshipStatuses status;
}
