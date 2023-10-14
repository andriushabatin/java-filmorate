MERGE INTO friendship_status
KEY(status_id)
VALUES ('1', 'NOT_CONFIRMED');

MERGE INTO friendship_status
KEY(status_id)
VALUES ('2', 'CONFIRMED');

MERGE INTO genre
KEY(genre_id)
VALUES ('1', 'Комедия');

MERGE INTO genre
KEY(genre_id)
VALUES ('2', 'Драма');

MERGE INTO genre
KEY(genre_id)
VALUES ('3', 'Мультфильм');

MERGE INTO genre
KEY(genre_id)
VALUES ('4', 'Триллер');

MERGE INTO genre
KEY(genre_id)
VALUES ('5', 'Документальный');

MERGE INTO genre
KEY(genre_id)
VALUES ('6', 'Боевик');

MERGE INTO rating
KEY(rating_id)
VALUES ('1', 'G');

MERGE INTO rating
KEY(rating_id)
VALUES ('2', 'PG');

MERGE INTO rating
KEY(rating_id)
VALUES ('3', 'PG-13');

MERGE INTO rating
KEY(rating_id)
VALUES ('4', 'R');

MERGE INTO rating
KEY(rating_id)
VALUES ('5', 'NC-17');

MERGE INTO operations KEY(id) VALUES
(1, 'ADD'),
(2, 'REMOVE'),
(3, 'UPDATE');
MERGE INTO events_types KEY(id) VALUES
(1, 'LIKE'),
(2, 'REVIEW'),
(3, 'FRIEND');