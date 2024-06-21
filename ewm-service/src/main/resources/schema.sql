DROP TABLE IF EXISTS compilations_events;
DROP TABLE IF EXISTS event_requests;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS compilations;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(250),
    email VARCHAR(254) UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN,
    title VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation VARCHAR(2000),
    description VARCHAR(7000),
    category_id BIGINT,
    compilation_id BIGINT,
    created_on TIMESTAMP,
    event_date TIMESTAMP,
    initiator_id BIGINT,
    location_lon BIGINT,
    location_lat BIGINT,
    paid BOOLEAN,
    participants_limit INTEGER,
    published_on TIMESTAMP,
    request_moderation bool,
    confirmed_requests BIGINT,
    state VARCHAR(255),
    title VARCHAR(120),
    views INTEGER,
    CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT fk_events_to_compilations FOREIGN KEY (compilation_id) REFERENCES compilations (id)
);

CREATE TABLE IF NOT EXISTS event_requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id BIGINT,
    created TIMESTAMP,
    requester_id BIGINT,
    status VARCHAR(255),
    CONSTRAINT fk_requests_to_events FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT uq_id_initiator UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
    compilation_id BIGINT,
    event_id BIGINT,
    CONSTRAINT fk_compilations_events_to_events FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_compilations_events_to_compilations FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    CONSTRAINT uq_relation UNIQUE(compilation_id, event_id)
);
