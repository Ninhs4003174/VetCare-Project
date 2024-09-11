package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.PetRecord;
import java.util.List;

public interface PetRecordService {

    // Get all pet records
    List<PetRecord> getAllPetRecords();

    // Get a pet record by ID
    PetRecord getPetRecordById(Long id);

    // Save a new pet record
    void save(PetRecord petRecord);

    // Update an existing pet record
    void update(PetRecord petRecord);

    // Delete a pet record by ID
    void delete(Long id);
}
