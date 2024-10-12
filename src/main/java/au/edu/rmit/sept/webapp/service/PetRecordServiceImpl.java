package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.PetRecord;
import au.edu.rmit.sept.webapp.repository.PetRecordRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetRecordServiceImpl implements PetRecordService {

    @Autowired
    private PetRecordRepository petRecordRepository;

    // Fetch all pet records from the repository
    @Override
    public List<PetRecord> getAllPetRecords() {
        return petRecordRepository.findAll();
    }

    // Fetch a single pet record by its ID
    @Override
    public PetRecord getPetRecordById(Long id) {
        Optional<PetRecord> optionalPetRecord = petRecordRepository.findById(id);
        return optionalPetRecord.orElse(null);
    }

    // Fetch all pet records for a specific user ID
    @Override
    public List<PetRecord> getPetRecordsByUserId(Long userId) {
        return petRecordRepository.findByUserId(userId);
    }

    // Save a new pet record to the repository
    @Override
    public void save(PetRecord petRecord) {
        petRecordRepository.save(petRecord);
    }

    // Update an existing pet record in the repository
    @Override
    public void update(PetRecord petRecord) {
        // Check if the pet record exists before updating
        if (petRecordRepository.existsById(petRecord.getId())) {
            petRecordRepository.save(petRecord);
        }
    }

    // Delete a pet record by its ID
    @Override
    public void delete(Long id) {
        petRecordRepository.deleteById(id);
    }
}
