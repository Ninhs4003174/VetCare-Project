-- Update existing records with the owner information
UPDATE pet_record
SET owner = 'John Doe'
WHERE name = 'Buddy';

UPDATE pet_record
SET owner = 'Emily Green'
WHERE name = 'Luna';