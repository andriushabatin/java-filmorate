MERGE INTO friendship_status
KEY(status_id)
VALUES ('1', 'NOT_CONFIRMED');

MERGE INTO friendship_status
KEY(status_id)
VALUES ('2', 'CONFIRMED');

MERGE INTO genre
KEY(genre_id)
VALUES ('1', 'COMEDY');

MERGE INTO genre
KEY(genre_id)
VALUES ('2', 'DRAMA');

MERGE INTO genre
KEY(genre_id)
VALUES ('3', 'CARTOON');

MERGE INTO genre
KEY(genre_id)
VALUES ('4', 'THRILLER');

MERGE INTO genre
KEY(genre_id)
VALUES ('5', 'DOCUMENTARY');

MERGE INTO genre
KEY(genre_id)
VALUES ('6', 'ACTION');

MERGE INTO rating
KEY(rating_id)
VALUES ('1', '13+');