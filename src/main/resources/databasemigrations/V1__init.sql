CREATE TABLE devices (
    id text NOT NULL PRIMARY KEY,
    status text NOT NULL DEFAULT 'ACTIVE',
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE device_certificates (
    id text NOT NULL PRIMARY KEY,
    device_id uuid NOT NULL,
    request_id text NOT NULL,
    serial_number text NOT NULL,
    expiration float NOT NULL,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    foreign key(device_id) references devices(id)
);

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
