CREATE TABLE IF NOT EXISTS vetbooking (
    id SERIAL PRIMARY KEY,                     -- Unique identifier for each booking
    vet_user_id BIGINT NOT NULL,               -- Link to the vet user making the booking
    clinic_id BIGINT NOT NULL,                 -- Clinic where the vet works, must not be null
    service_type VARCHAR(100),                 -- Type of service being booked
    address VARCHAR(255),                     -- Address of the clinic
    phone_number VARCHAR(20),                 -- Phone number of the clinic
    email VARCHAR(100),                       -- Email of the clinic
                        -- Link to the vet user account
    FOREIGN KEY (vet_user_id) REFERENCES vet_users(id) -- Link to the vet user
   
);
