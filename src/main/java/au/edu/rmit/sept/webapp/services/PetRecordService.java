package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.PetRecord;

import java.util.List;

public interface PetRecordService {
    List<PetRecord> getAllPetRecords();

    PetRecord getPetRecordById(Long id);

    void savePetRecord(PetRecord petRecord);
}
