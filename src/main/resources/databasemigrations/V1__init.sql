CREATE TABLE devices (
    id uuid NOT NULL PRIMARY KEY,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE user_devices (
    id uuid NOT NULL PRIMARY KEY,
    user_id uuid NOT NULL,
    device_id uuid NOT NULL,
    hashed_password text NOT NULL,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY(device_id) REFERENCES devices(id)
);
