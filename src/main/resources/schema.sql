CREATE TABLE IF NOT EXISTS CARS_MOVIES_ENTITY (
    id CHAR(36) DEFAULT (UUID()) PRIMARY KEY,
    car_movie_name VARCHAR(255) NOT NULL,
    car_movie_year CHAR(4) NOT NULL,
    duration INT NOT NULL CHECK (duration > 0)
);
