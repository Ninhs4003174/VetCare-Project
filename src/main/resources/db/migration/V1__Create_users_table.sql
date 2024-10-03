CREATE TABLE IF NOT EXISTS vet_users (
    id SERIAL PRIMARY KEY,                  -- Unique identifier for each user
    username VARCHAR(100) NOT NULL,         -- User's username, must not be null
    password VARCHAR(100) NOT NULL,         -- User's password, must not be null
    clinic_id BIGINT,                       -- Optional: Link to clinic for vet users
    role VARCHAR(50) DEFAULT 'CLIENT',   -- Role of the user (admin, receptionist, vet)
    address VARCHAR(255),                   -- User's address
    email VARCHAR(100),                     -- User's email
    phone_number VARCHAR(20)             -- User's phone number
);
