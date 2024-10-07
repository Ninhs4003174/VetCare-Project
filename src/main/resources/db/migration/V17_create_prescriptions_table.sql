CREATE TABLE IF NOT EXISTS prescriptions (
    id SERIAL PRIMARY KEY,                          -- Unique identifier for each prescription
    user_id BIGINT NOT NULL,                        -- Foreign key linking to vet_users (pet owner)
    vet_id BIGINT NOT NULL,                         -- Foreign key linking to vet_users (the vet who prescribed it)
    pet_name VARCHAR(100),                          -- Name of the pet
    medication_name VARCHAR(255) NOT NULL,          -- Name of the medication
    dosage VARCHAR(255) NOT NULL,                   -- Dosage instructions
    refills_available INT DEFAULT 0,                -- Number of refills available
    refill_request BOOLEAN DEFAULT FALSE,           -- Tracks if a refill has been requested
    delivery_status VARCHAR(50) DEFAULT 'PENDING',  -- Status of the prescription delivery (PENDING, SHIPPED, DELIVERED)
    created_at TIMESTAMP DEFAULT NOW(),             -- Timestamp of when the prescription was created
    updated_at TIMESTAMP DEFAULT NOW()              -- Timestamp for updates
);

-- Add foreign key constraints to link to vet_users table
ALTER TABLE prescriptions
ADD CONSTRAINT fk_user
FOREIGN KEY (user_id) REFERENCES vet_users(id)
ON DELETE CASCADE;

ALTER TABLE prescriptions
ADD CONSTRAINT fk_vet
FOREIGN KEY (vet_id) REFERENCES vet_users(id)
ON DELETE SET NULL;
