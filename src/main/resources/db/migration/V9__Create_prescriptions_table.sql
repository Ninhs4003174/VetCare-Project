CREATE TABLE prescriptions (
    prescription_id SERIAL PRIMARY KEY,
    pet_id INT REFERENCES pets(pet_id),
    vet_id INT REFERENCES vets(vet_id),
    medication_name VARCHAR(100) NOT NULL,
    dosage VARCHAR(50),
    instructions TEXT,
    issue_date DATE,
    expiration_date DATE,
    refill_status VARCHAR(20) CHECK (refill_status IN ('Pending', 'Approved', 'Denied')),
    delivery_address VARCHAR(255)
);
