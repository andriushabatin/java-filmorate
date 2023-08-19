package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.FriendshipStatuses;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friendship_status")
public class FriendshipStatus {

    @Id
    @Column(name = "status_id")
    int status_id;

    @Column(name = "status")
    FriendshipStatuses status;

    @OneToOne
    Friendship friendship;
}
