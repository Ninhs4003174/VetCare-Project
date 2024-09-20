package au.edu.rmit.sept.webapp.service;

import java.util.List;

import au.edu.rmit.sept.webapp.model.PetRecord;

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
