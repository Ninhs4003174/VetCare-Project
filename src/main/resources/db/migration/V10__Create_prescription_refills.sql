CREATE TABLE prescription_refills (
    refill_id SERIAL PRIMARY KEY,
    prescription_id INT REFERENCES prescriptions(prescription_id),
    request_date DATE,
    status VARCHAR(20) CHECK (status IN ('Requested', 'In Progress', 'Completed', 'Denied')),
    delivery_date DATE,
    delivery_status VARCHAR(20) CHECK (delivery_status IN ('Pending', 'Delivered'))
);
