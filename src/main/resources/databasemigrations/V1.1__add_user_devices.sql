CREATE TABLE user_devices (
    id text NOT NULL PRIMARY KEY,
    status text NOT NULL DEFAULT 'ACTIVE',
    user_id text NOT NULL,
    device_id text NOT NULL,
    hashed_password text NOT NULL,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY(device_id) REFERENCES devices(id)
);
