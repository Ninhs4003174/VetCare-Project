CREATE TABLE IF NOT EXISTS vetbooking (
    id SERIAL PRIMARY KEY,                     -- Unique identifier for each booking
    vet_user_id BIGINT NOT NULL,              -- Link to the vet user making the booking
    clinic_id BIGINT NOT NULL,                 -- Clinic where the vet works, must not be null
    service_type VARCHAR(100),                 -- Type of service being booked
    FOREIGN KEY (vet_user_id) REFERENCES vet_users(id) -- Link to the user who made the booking
);
