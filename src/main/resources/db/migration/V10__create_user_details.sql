CREATE TABLE IF NOT EXISTS vet_user_details (
    id SERIAL PRIMARY KEY,                  -- Unique identifier for each detail entry
    vet_user_id BIGINT NOT NULL,            -- Link to the vet user
    address VARCHAR(255),                    -- Address of the vet user
    phone_number VARCHAR(20),                -- Phone number of the vet user
    FOREIGN KEY (vet_user_id) REFERENCES vet_users(id) -- Link to the vet user
);
