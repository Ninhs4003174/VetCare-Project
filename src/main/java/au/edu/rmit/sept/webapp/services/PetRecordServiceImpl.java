package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.PetRecord;
import au.edu.rmit.sept.webapp.repositories.PetRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetRecordServiceImpl implements PetRecordService {

    @Autowired
    private PetRecordRepository petRecordRepository;

    @Override
    public List<PetRecord> getAllPetRecords() {
        return petRecordRepository.findAll();
    }

    @Override
    public PetRecord getPetRecordById(Long id) {
        return petRecordRepository.findById(id).orElse(null);
    }

    @Override
    public void savePetRecord(PetRecord petRecord) {
        petRecordRepository.save(petRecord);
    }
}
