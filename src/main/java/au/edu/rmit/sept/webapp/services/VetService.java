package au.edu.rmit.sept.webapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.Vet;
import au.edu.rmit.sept.webapp.repositories.VetRepository;

@Service
public class VetService {

    private final VetRepository repository;

    @Autowired
    public VetService(VetRepository repository) {
        this.repository = repository;
    }

    public List<Vet> getAllVets() {
        return repository.findAll();
    }
}
