CREATE TABLE "film" (
  "id" int NOT NULL PRIMARY KEY,
  "name" varchar(255),
  "description" varchar(255),
  "release_date" date,
  "duration" int,
  "rating_id" int
);

CREATE TABLE "user" (
  "id" int NOT NULL PRIMARY KEY,
  "email" varchar(255),
  "login" varchar(255),
  "name" varchar(255),
  "birthday" date
);

CREATE TABLE "genre" (
  "genre_id" int NOT NULL PRIMARY KEY,
  "genre_name" varchar(255)
);

CREATE TABLE "film_genres" (
  "film_id" int NOT NULL ,
  "genre_id" int NOT NULL
);

CREATE TABLE "rating" (
  "rating_id" int NOT NULL PRIMARY KEY,
  "rating_name" varchar(255) NOT NULL
);

CREATE TABLE "friends" (
  "user_id" int NOT NULL,
  "friend_id" int NOT NULL
);

ALTER TABLE "film_genres" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "film_genres" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("genre_id");

ALTER TABLE "film" ADD FOREIGN KEY ("rating_id") REFERENCES "rating" ("rating_id");

CREATE TABLE "user_friends" (
  "user_id" int,
  "friends_user_id" int,
  PRIMARY KEY ("user_id", "friends_user_id")
);

ALTER TABLE "user_friends" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "user_friends" ADD FOREIGN KEY ("friends_user_id") REFERENCES "friends" ("user_id");


CREATE TABLE "friends_user" (
  "friends_friend_id" int,
  "user_id" int,
  PRIMARY KEY ("friends_friend_id", "user_id")
);

ALTER TABLE "friends_user" ADD FOREIGN KEY ("friends_friend_id") REFERENCES "friends" ("friend_id");

ALTER TABLE "friends_user" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

