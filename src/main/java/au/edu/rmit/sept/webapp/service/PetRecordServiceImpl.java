package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.PetRecord;
import au.edu.rmit.sept.webapp.repository.PetRecordRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetRecordServiceImpl implements PetRecordService {

    @Autowired
    private PetRecordRepository repository;

    @Override
    public List<PetRecord> getAllRecords() {
        return repository.findAll();
    }

    @Override
    public PetRecord saveRecord(PetRecord record) {
        return repository.save(record);
    }

    @Override
    public void deleteRecord(Long id) {
        repository.deleteById(id);
    }

    @Override
    public PetRecord getRecordById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
