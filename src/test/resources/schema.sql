CREATE TABLE if not exists genres (
  genre_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  genre varchar(255) NOT NULL
);

CREATE TABLE if not exists mpa_rating (
  mpa_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  mpa varchar(255) NOT NULL
);

CREATE TABLE if not exists users (
  user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  email varchar(255) NOT NULL UNIQUE,
  login varchar(255) NOT NULL UNIQUE,
  name varchar(255) NOT NULL,
  birthday date
);

-- Затем создаем таблицы, которые ссылаются на другие
CREATE TABLE if not exists films (
  film_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name varchar(255) NOT NULL,
  description varchar(255) NOT NULL,
  release_date date NOT NULL,
  duration int NOT NULL,
  mpa_id int NOT NULL,
  FOREIGN KEY (mpa_id) REFERENCES mpa_rating (mpa_id)
);

CREATE TABLE if not exists film_genres (
  film_id int NOT NULL,
  genre_id int NOT NULL,
  PRIMARY KEY(film_id, genre_id),
  FOREIGN KEY (film_id) REFERENCES films (film_id),
  FOREIGN KEY (genre_id) REFERENCES genres (genre_id)
);

CREATE TABLE if not exists friends (
  user_id int NOT NULL,
  friend_id int NOT NULL,
  PRIMARY KEY (user_id, friend_id),
  FOREIGN KEY (user_id) REFERENCES users (user_id),
  FOREIGN KEY (friend_id) REFERENCES users (user_id)
);

CREATE TABLE if not exists film_likes (
  film_id int NOT NULL,
  user_id int NOT NULL,
  PRIMARY KEY (film_id, user_id),
  FOREIGN KEY (film_id) REFERENCES films (film_id),
  FOREIGN KEY (user_id) REFERENCES users (user_id)
);