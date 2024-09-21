package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.User; // Import User model
import au.edu.rmit.sept.webapp.repository.PetRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public void addPet(Pet pet) {
        petRepository.save(pet);
    }

    public Pet findPetById(Long id) {
        return petRepository.findById(id).orElse(null);
    }

    public void updatePet(Pet pet) {
        petRepository.save(pet);
    }

    // New method to find pets by user
    public List<Pet> findPetsByUser(User user) {
        return petRepository.findByOwner(user); // Ensure this method is implemented in your PetRepository
    }
}
