package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.converter.DurationDeserializer;
import ru.yandex.practicum.filmorate.converter.DurationSerializer;


import java.time.Duration;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private int id;

    private String name;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date releaseDate;


    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

    //private List<Genre> genre;

    //private int rating;

}
