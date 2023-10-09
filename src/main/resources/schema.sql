
drop table IF EXISTS user_table CASCADE;
drop table IF EXISTS users CASCADE;
drop table IF EXISTS friendship_status CASCADE;
drop table IF EXISTS friendship CASCADE;
drop table IF EXISTS rating CASCADE;
drop table IF EXISTS film CASCADE;
drop table IF EXISTS genre CASCADE;
drop table IF EXISTS film_genre  CASCADE;
drop table IF EXISTS likes CASCADE;
drop table IF EXISTS reviews CASCADE;

drop table IF EXISTS feed CASCADE;
drop table IF EXISTS operations CASCADE;
drop table IF EXISTS events_types CASCADE;


CREATE TABLE IF NOT EXISTS users (
  user_id integer generated by default as identity NOT NULL,
  email varchar(40),
  login varchar(40),
  name varchar(40),
  birthday date,
  PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS friendship_status (
  status_id integer NOT NULL,
  status varchar(20),
  PRIMARY KEY (status_id)
);

CREATE TABLE IF NOT EXISTS friendship (
  user_id integer NOT NULL,
  friend_id integer NOT NULL,
  status varchar(20) NOT NULL,
  PRIMARY KEY (user_id, friend_id),
  FOREIGN KEY (user_id) REFERENCES users (user_id),
  FOREIGN KEY (friend_id) REFERENCES users (user_id),
  FOREIGN KEY (status) REFERENCES friendship_status (status_id)
);

CREATE TABLE IF NOT EXISTS rating (
  rating_id integer NOT NULL,
  rating varchar(10),
  PRIMARY KEY (rating_id)
 );

CREATE TABLE IF NOT EXISTS film (
  film_id integer generated by default as identity NOT NULL,
  name varchar(40),
  description varchar(200),
  release date,
  duration long,
  rate integer,
  rating_id integer,
  PRIMARY KEY (film_id),
  FOREIGN KEY (rating_id) REFERENCES rating (rating_id)
);

CREATE TABLE IF NOT EXISTS genre (
  genre_id integer NOT NULL,
  genre varchar(20),
  PRIMARY KEY (genre_id)
);

CREATE TABLE IF NOT EXISTS film_genre (
  film_id integer NOT NULL,
  genre_id varchar(10) NOT NULL,
  PRIMARY KEY (film_id, genre_id),
  FOREIGN KEY (film_id) REFERENCES film (film_id),
  FOREIGN KEY (genre_id) REFERENCES genre (genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
  film_id integer NOT NULL,
  user_id integer NOT NULL,
  PRIMARY KEY (film_id, user_id),
  FOREIGN KEY (film_id) REFERENCES film (film_id),
  FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS reviews (
    review_id int generated by default as identity primary key,
    film_id  int,
    user_id int,
    content varchar,
    is_positive boolean,
    useful int,
    FOREIGN KEY (film_id) REFERENCES film (film_id) on delete cascade,
    FOREIGN KEY (user_id) REFERENCES users (user_id) on delete cascade
);

CREATE TABLE IF NOT EXISTS operations (
    id integer PRIMARY KEY NOT NULL,
    operation varchar(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS events_types (
    id integer PRIMARY KEY NOT NULL,
    type varchar(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS feed (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id integer NOT NULL,
    timestamp bigint NOT NULL,
    type_id integer NOT NULL,
    operation_id integer NOT NULL,
    entity_id integer NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (type_id) REFERENCES events_types (id) ON DELETE CASCADE,
    FOREIGN KEY (operation_id) REFERENCES operations (id) ON DELETE CASCADE
);