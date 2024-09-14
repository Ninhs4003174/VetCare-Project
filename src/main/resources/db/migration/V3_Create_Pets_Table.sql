CREATE TABLE pets (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    age INT,
    bio TEXT,
    owner_id BIGINT,
    FOREIGN KEY (owner_id) REFERENCES vet_users(id)
);
