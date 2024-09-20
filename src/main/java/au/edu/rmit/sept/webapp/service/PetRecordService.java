package au.edu.rmit.sept.webapp.service;

import java.util.List;

import au.edu.rmit.sept.webapp.model.PetRecord;

public interface PetRecordService {
    List<PetRecord> getAllRecords();

    PetRecord saveRecord(PetRecord record);

    void deleteRecord(Long id);

    PetRecord getRecordById(Long id);
}
