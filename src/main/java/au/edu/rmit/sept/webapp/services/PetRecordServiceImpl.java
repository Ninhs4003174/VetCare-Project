package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.PetRecord;
import au.edu.rmit.sept.webapp.repositories.PetRecordRepository;
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

    // Save a new pet record to the repository
    @Override
    public void save(PetRecord petRecord) {
        petRecordRepository.save(petRecord);
    }

    // Update an existing pet record in the repository
    @Override
    public void update(PetRecord petRecord) {
        petRecordRepository.save(petRecord);
    }

    // Delete a pet record by its ID
    @Override
    public void delete(Long id) {
        petRecordRepository.deleteById(id);
    }
}
