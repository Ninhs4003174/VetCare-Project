package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Vet;
import au.edu.rmit.sept.webapp.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VetService {

    @Autowired
    private VetRepository vetRepository;

    // Method to get all vets
    public List<Vet> getAllVets() {
        return vetRepository.findAll();
    }

    // Method to get a vet by id
    public Vet getVetById(Long id) {
        return vetRepository.findById(id).orElse(null);
    }
}
