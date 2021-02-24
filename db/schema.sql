CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   date TIMESTAMP
);

create TABLE city (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE photo (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT,
    photo_id INTEGER REFERENCES photo("id")
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT UNIQUE,
    password TEXT
);

alter table candidate add column city_id INTEGER REFERENCES city(id);