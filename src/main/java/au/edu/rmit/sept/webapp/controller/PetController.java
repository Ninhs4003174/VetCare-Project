package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PetController {

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    @GetMapping("/edit-pet/{petId}")
    public String showEditPetForm(@PathVariable Long petId, Model model) {
        Pet pet = petService.findPetById(petId);
        if (pet == null) {
            return "error"; // or redirect to an error page
        }
        model.addAttribute("pet", pet);
        return "edit-pet"; // Return the template for editing
    }

    @PostMapping("/edit-pet")
    public String editPet(@RequestParam Long petId,
            @RequestParam String petName,
            @RequestParam String petType,
            @RequestParam int petAge,
            @RequestParam String petBio,
            Model model) {
        try {
            Pet pet = petService.findPetById(petId);
            if (pet == null) {
                model.addAttribute("message", "Pet not found");
                return "error"; // Handle the error appropriately
            }

            // Update pet details
            pet.setName(petName);
            pet.setType(petType);
            pet.setAge(petAge);
            pet.setBio(petBio);

            petService.updatePet(pet); // Ensure this method exists in your service

            return "redirect:/profile"; // Redirect to the profile page after editing
        } catch (Exception e) {
            logger.error("Failed to edit pet", e);
            model.addAttribute("message", "Failed to edit pet: " + e.getMessage());
            return "edit-pet"; // Return to the edit pet form on error
        }
    }

    @GetMapping("/add-pet")
    public String showAddPetForm(Model model) {
        // Get the logged-in user's username
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Fetch the user details
        User currentUser = userService.findByUsername(username);
        model.addAttribute("user", currentUser);
        return "add-pet"; // Return the template for adding pets
    }

    @PostMapping("/add-pet")
    public String addPet(@RequestParam String petName,
            @RequestParam String petType,
            @RequestParam int petAge,
            @RequestParam String petBio,
            Model model) {
        try {
            // Get the logged-in user's username
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }

            User user = userService.findByUsername(username);
            if (user == null) {
                model.addAttribute("message", "User not found");
                return "add-pet"; // Return to form on error
            }

            Pet newPet = new Pet(petName, petType, petAge, petBio, user);
            petService.addPet(newPet); // Save new pet to the database

            return "redirect:/profile"; // Redirect back to profile
        } catch (Exception e) {
            logger.error("Failed to add pet", e);
            model.addAttribute("message", "Failed to add pet: " + e.getMessage());
            return "add-pet"; // Return to the add pet form on error
        }
    }

}
