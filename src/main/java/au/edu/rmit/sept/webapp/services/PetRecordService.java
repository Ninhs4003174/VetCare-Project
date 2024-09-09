package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.PetRecord;
import java.util.List;

public interface PetRecordService {
    List<PetRecord> getAllRecords();

    PetRecord saveRecord(PetRecord record);

    void deleteRecord(Long id);

    PetRecord getRecordById(Long id);
}
