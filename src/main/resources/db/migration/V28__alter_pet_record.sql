ALTER TABLE pet_record
ADD COLUMN clinic_id BIGINT REFERENCES vet_users(clinic_id);
