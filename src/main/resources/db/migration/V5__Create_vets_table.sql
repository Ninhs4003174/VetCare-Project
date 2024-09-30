CREATE TABLE IF NOT EXISTS vet (
    address VARCHAR(255),                     -- Address of the clinic
    phone_number VARCHAR(20),                 -- Phone number of the clinic
    email VARCHAR(100),                       -- Email of the clinic
    vet_user_id BIGINT,                      -- Link to the vet user account
    FOREIGN KEY (vet_user_id) REFERENCES vet_users(id) -- Link to the vet user
);
