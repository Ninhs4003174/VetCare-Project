CREATE TABLE IF NOT EXISTS prescription_requests (
    request_id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    pet_id BIGINT NOT NULL,
    medication VARCHAR(255) NOT NULL,
    reason TEXT NOT NULL,
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'Pending',
    FOREIGN KEY (user_id) REFERENCES vet_users(id),
    FOREIGN KEY (pet_id) REFERENCES pets(id)
);
