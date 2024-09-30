-- V3__Add_Foreign_Key_To_Vet_Users.sql

ALTER TABLE vet_users
ADD CONSTRAINT fk_clinic_id
FOREIGN KEY (clinic_id) REFERENCES vet(clinic_id);