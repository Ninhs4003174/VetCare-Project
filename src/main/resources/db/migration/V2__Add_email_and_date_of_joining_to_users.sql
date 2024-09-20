-- Filename: V2__Add_email_and_date_of_joining_to_users.sql

ALTER TABLE vet_users
ADD COLUMN email VARCHAR(100),
ADD COLUMN date_of_joining DATE;
