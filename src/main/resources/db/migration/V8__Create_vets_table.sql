CREATE TABLE vet (
    vet_id SERIAL PRIMARY KEY,
    clinic_name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(20),
    email VARCHAR(100)
);


INSERT INTO vet (clinic_name, address, phone_number, email)
 VALUES ('Healthy Pets Clinic', '123 Pet Street, Petville', '123-456-7890', 'contact@healthypets.com'),
('Paws and Claws Veterinary', '456 Animal Avenue, Petcity', '987-654-3210', 'info@pawsandclaws.com'),
('The Animal Hospital', '789 Vet Lane, Petburg', '555-123-4567', 'info@animalhospital.com'),
('Companion Care Veterinary', '101 Pet Plaza, Petropolis', '555-987-6543', 'support@companioncare.com');