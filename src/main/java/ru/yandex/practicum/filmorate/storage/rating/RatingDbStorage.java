package ru.yandex.practicum.filmorate.storage.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.util.List;
import java.util.Optional;

@Component
public class RatingDbStorage {

    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> getMpa() {
        return ratingRepository.findAll();
    }

    public Rating getMpaById(int id) {

        Optional<Rating> ratingDb = this.ratingRepository.findById(id);
        if (ratingDb.isPresent()) {
            return ratingDb.get();
        } else {
            throw new NotFoundException("Genre not found with id : " + id);
        }
    }
}
