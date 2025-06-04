MERGE INTO genres AS target
USING (VALUES
  (1, 'Комедия'),
  (2, 'Драма'),
  (3, 'Мультфильм'),
  (4, 'Триллер'),
  (5, 'Документальный'),
  (6, 'Боевик')
) AS source (genre_id, genre)
ON target.genre_id = source.genre_id
WHEN MATCHED THEN
  UPDATE SET genre = source.genre
WHEN NOT MATCHED THEN
  INSERT (genre_id, genre) VALUES (source.genre_id, source.genre);

MERGE INTO mpa_rating AS target
USING (VALUES (1, 'G'),
(2, 'PG'),
(3, 'PG-13'),
(4, 'R'),
(5, 'NC-17')
) AS source (mpa_id, mpa)
ON target.mpa_id = source.mpa_id
WHEN MATCHED THEN
  UPDATE SET mpa = source.mpa
WHEN NOT MATCHED THEN
  INSERT (mpa_id, mpa) VALUES (source.mpa_id, source.mpa);