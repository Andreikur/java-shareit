CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE  IF NOT EXISTS items
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name CHARACTER VARYING (50)  NOT NULL,
    description CHARACTER VARYING (255),
    available BOOLEAN,
    owner_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    request_id INTEGER
);

CREATE TABLE  IF NOT EXISTS bookings
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    --start_date TIMESTAMP WITHOUT TIME ZONE CHECK (start_date > NOW()),
    --nd_date TIMESTaAMP WITHOUT TIME ZONE CHECK (end_date > NOW()),
    start_date TIMESTAMP WITHOUT TIME ZONE not null,
    end_date TIMESTAMP WITHOUT TIME ZONE not null,
    item_id  BIGINT REFERENCES items (id) ON DELETE CASCADE,
    booker_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    status VARCHAR
);

CREATE TABLE  IF NOT EXISTS requests
(
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  CHARACTER VARYING (255) NOT NULL,
    requestor_id INTEGER NOT NULL
);

CREATE TABLE  IF NOT EXISTS comments
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    comment_text VARCHAR NOT NULL,
    item_id BIGINT REFERENCES items (id) ON DELETE CASCADE,
    author_id BIGINT REFERENCES users (id) ON DELETE CASCADE
);
