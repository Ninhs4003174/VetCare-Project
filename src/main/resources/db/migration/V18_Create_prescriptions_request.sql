CREATE TABLE IF NOT EXISTS prescription_requests (
    id SERIAL PRIMARY KEY,                     -- Unique identifier for each prescription request
    prescription_id BIGINT NOT NULL,           -- Foreign key linking to the prescription
    pet_name VARCHAR(100),               -- Foreign key linking to vet_users (pet owner)
    status VARCHAR(50) DEFAULT 'PENDING',      -- Status of the request (PENDING, APPROVED, DENIED)
    requested_at TIMESTAMP DEFAULT NOW(),      -- Timestamp of when the request was made
    processed_at TIMESTAMP,                    -- Timestamp for when the request is processed
                      -- Foreign key linking to vet_users (vet who processed the request)
    
    -- Additional details about the request if needed
    comments TEXT
);

-- Add foreign key constraints
ALTER TABLE prescription_requests
ADD CONSTRAINT fk_prescription
FOREIGN KEY (prescription_id) REFERENCES prescriptions(id)
ON DELETE CASCADE;

