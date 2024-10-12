-- Make the clinic_id column in vet_users unique
ALTER TABLE vet_users
ADD CONSTRAINT unique_clinic_id UNIQUE (clinic_id);

-- Add the clinic_id column to pet_record and reference vet_users
ALTER TABLE pet_record
ADD COLUMN clinic_id BIGINT REFERENCES vet_users(clinic_id);

ALTER TABLE pet_record
ADD COLUMN vet_id BIGINT,
ADD CONSTRAINT fk_pet_record_vet
FOREIGN KEY (vet_id)
REFERENCES vet(vet_id);