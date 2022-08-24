CREATE TABLE IF NOT EXISTS event (
    event_id VARCHAR(255) PRIMARY KEY NOT NULL,
    event_action VARCHAR(255) NOT NULL,
    event_subject VARCHAR(255) NOT NULL,
    event_recipient VARCHAR(255) NOT NULL,
    event_timestamp TIMESTAMP NOT NULL
);
