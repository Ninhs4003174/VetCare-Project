CREATE TABLE VETS (
    ID INT PRIMARY KEY AUTO_INCREMENT,  -- Unique identifier for each vet, with auto-increment
    NAME VARCHAR(100) NOT NULL,         -- Vet's name, must not be null
    CLINIC VARCHAR(100) NOT NULL        -- Clinic where the vet works, must not be null
);