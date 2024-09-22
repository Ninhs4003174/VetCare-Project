CREATE TABLE VETBOOKING (
    ID SERIAL PRIMARY KEY,  -- Unique identifier for each vet, auto-incremented
    NAME VARCHAR(100) NOT NULL,         -- Vet's name, must not be null
    CLINIC VARCHAR(100) NOT NULL ,       -- Clinic where the vet works, must not be null
    service_type VARCHAR(100)
);