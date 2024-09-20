CREATE TABLE pet_record (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    breed VARCHAR(255),
    date_of_birth VARCHAR(255),
    last_visit VARCHAR(255),
    allergies VARCHAR(255),
    prescriptions VARCHAR(255),
    vaccination_history VARCHAR(255),
    recent_tests VARCHAR(255),
    recent_surgeries VARCHAR(255),
    dietary_recommendations VARCHAR(255),
    notes VARCHAR(255),
    veterinarian VARCHAR(255)
);
