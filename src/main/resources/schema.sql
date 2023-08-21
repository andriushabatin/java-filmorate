drop table IF EXISTS user_table CASCADE;
drop table IF EXISTS friendship_status CASCADE;
drop table IF EXISTS friendship CASCADE;
drop table IF EXISTS rating CASCADE;
drop table IF EXISTS film CASCADE;
drop table IF EXISTS genre CASCADE;
drop table IF EXISTS film_genre  CASCADE;
drop table IF EXISTS likes CASCADE;


CREATE TABLE IF NOT EXISTS user_table (
  user_id integer NOT NULL,
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
  FOREIGN KEY (user_id) REFERENCES user_table (user_id),
  FOREIGN KEY (friend_id) REFERENCES user_table (user_id),
  FOREIGN KEY (status) REFERENCES friendship_status (status_id)
);

CREATE TABLE IF NOT EXISTS rating (
  rating_id integer NOT NULL,
  rating varchar(10),
  PRIMARY KEY (rating_id)
 );

CREATE TABLE IF NOT EXISTS film (
  film_id integer NOT NULL,
  name varchar(40),
  description varchar(200),
  release date,
  duration long,
  rating integer,
  PRIMARY KEY (film_id),
  FOREIGN KEY (rating) REFERENCES rating (rating_id)
);

CREATE TABLE IF NOT EXISTS genre (
  genre_id integer NOT NULL,
  genre varchar(20),
  PRIMARY KEY (genre_id)
);

CREATE TABLE IF NOT EXISTS film_genre (
  film_id integer NOT NULL,
  genre varchar(10) NOT NULL,
  PRIMARY KEY (film_id, genre),
  FOREIGN KEY (film_id) REFERENCES film (film_id),
  FOREIGN KEY (genre) REFERENCES genre (genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
  film_id integer NOT NULL,
  user_id integer NOT NULL,
  PRIMARY KEY (film_id, user_id),
  FOREIGN KEY (film_id) REFERENCES film (film_id),
  FOREIGN KEY (user_id) REFERENCES user_table (user_id)
);