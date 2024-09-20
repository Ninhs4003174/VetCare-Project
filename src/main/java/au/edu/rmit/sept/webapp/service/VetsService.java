package au.edu.rmit.sept.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.model.Vets;
import au.edu.rmit.sept.webapp.repository.VetsRepository;

@Service
public class VetsService {

    private final VetsRepository repository;

    @Autowired
    public VetsService(VetsRepository repository) {
        this.repository = repository;
    }

    public List<Vets> getAllVets() {
        return repository.findAll();
    }
}
