CREATE TABLE IF NOT EXISTS resource (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    url VARCHAR(255) NOT NULL,
    content TEXT,  -- Change the content column type to TEXT
    type VARCHAR(50)
);
