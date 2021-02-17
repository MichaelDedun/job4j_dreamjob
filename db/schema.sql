CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   date TIMESTAMP
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