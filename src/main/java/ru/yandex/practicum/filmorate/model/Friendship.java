package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.key.FriendshipPrimaryKey;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@Table(name = "friendship")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(FriendshipPrimaryKey.class)
public class Friendship {

    @Id
    @Column(name = "user_id")
    private int user_id;

    @Id
    @Column(name = "friend_id")
    private int friend_id;

    @Column(name = "status")
    private int status;

}
