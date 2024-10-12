DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'pet_record' AND column_name = 'owner') THEN
        ALTER TABLE pet_record ADD COLUMN owner VARCHAR(255);
    END IF;
END $$;