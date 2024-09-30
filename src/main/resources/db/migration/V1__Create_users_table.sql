CREATE TABLE IF NOT EXISTS vet_users (
    id SERIAL PRIMARY KEY,                  -- Unique identifier for each user
    username VARCHAR(100) NOT NULL,         -- User's username, must not be null
    password VARCHAR(100) NOT NULL,         -- User's password, must not be null
    clinic_id BIGINT,                       -- Optional: Link to clinic for vet users
    role VARCHAR(50) DEFAULT 'ROLE_USER'   -- Role of the user (admin, receptionist, vet)
);
