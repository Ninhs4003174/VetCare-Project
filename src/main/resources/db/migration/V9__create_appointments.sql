CREATE TABLE IF NOT EXISTS appointments (
    id BIGSERIAL PRIMARY KEY,   
     pet_name VARCHAR(255), 
     vet_id BIGINT,    
     date VARCHAR(255),           
     time VARCHAR(255),         
    status VARCHAR(255),      
    user_id BIGINT,            
    CONSTRAINT fk_vet_user FOREIGN KEY (user_id) REFERENCES vet_users(id) ON DELETE CASCADE,
    CONSTRAINT fk_vet FOREIGN KEY (vet_id) REFERENCES vetbooking(vet_user_id) ON DELETE CASCADE
);