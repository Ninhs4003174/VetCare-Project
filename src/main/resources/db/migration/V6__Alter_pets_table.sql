DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='vet_users' AND column_name='address') THEN
        ALTER TABLE vet_users ADD COLUMN address VARCHAR(255);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='vet_users' AND column_name='phone_number') THEN
        ALTER TABLE vet_users ADD COLUMN phone_number VARCHAR(20);
    END IF;
END $$;
