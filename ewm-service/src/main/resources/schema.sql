CREATE TABLE IF NOT EXISTS categories (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL UNIQUE,
  CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  annotation TEXT NOT NULL,
  category_id INT NOT NULL,
  confirmed_requests INT NOT NULL,
  created_on TIMESTAMP NOT NULL,
  description TEXT NOT NULL,
  event_date TIMESTAMP NOT NULL,
  initiator_id INT NOT NULL,
  location_lat FLOAT NOT NULL,
  location_lon FLOAT NOT NULL,
  paid boolean NOT NULL,
  participant_limit INT NOT NULL,
  published_on TIMESTAMP NOT NULL,
  request_moderation boolean NOT NULL,
  title VARCHAR(255) NOT NULL,
  state INT NOT NULL,
  views BIGINT NOT NULL,
  CONSTRAINT pk_events PRIMARY KEY (id),
  CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id),
  CONSTRAINT fk_initiator FOREIGN KEY (initiator_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS requests (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  created TIMESTAMP NOT NULL,
  event_id INT NOT NULL,
  requester_id INT NOT NULL,
  status VARCHAR(255) NOT NULL,
  CONSTRAINT pk_requests PRIMARY KEY (id),
  CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (id),
  CONSTRAINT fk_requester FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS compilations (
  id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  pinned BOOLEAN NOT NULL,
  title VARCHAR(50) NOT NULL,
  CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
  compilations_id INT NOT NULL,
  events_id INT NOT NULL,
  CONSTRAINT pk_compilations_events PRIMARY KEY (compilations_id, events_id),
  CONSTRAINT fk_compilations_events_to_compilations FOREIGN KEY (compilations_id) REFERENCES compilations (id),
  CONSTRAINT fk_compilations_events_to_events FOREIGN KEY (events_id) REFERENCES events (id)
);