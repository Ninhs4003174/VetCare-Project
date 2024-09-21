package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    /**
     * Adds a new pet to the database.
     *
     * @param pet the Pet object to be added.
     */
    public void addPet(Pet pet) {
        petRepository.save(pet);
    }

    /**
     * Finds a pet by its ID.
     *
     * @param id the ID of the pet to be found.
     * @return the Pet object if found, or null if not found.
     */
    public Pet findPetById(Long id) {
        return petRepository.findById(id).orElse(null);
    }

    /**
     * Updates an existing pet in the database.
     *
     * @param pet the Pet object with updated information.
     */
    public void updatePet(Pet pet) {
        petRepository.save(pet); // This will update the pet if it exists
    }
}
