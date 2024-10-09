package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.PrescriptionService;
import au.edu.rmit.sept.webapp.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PrescriptionService prescriptionService; // Ensure this is autowired

    @Autowired
    private PetService petService; // Ensure this is autowired

    @GetMapping("/profile")
    public String showProfile(Model model) {
        // Get the logged-in user's username
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Fetch the user details using the username
        User user = userService.findByUsername(username);

        // If user is null, handle gracefully
        if (user == null) {
            model.addAttribute("message", "User not found");
            return "error"; // Redirect to an error page or handle appropriately
        }

        // Add the user and user role to the model to pass to the view
        model.addAttribute("user", user);
        model.addAttribute("userRole", user.getRole());

        return "profile"; // The name of your Thymeleaf template
    }

    @GetMapping("/edit-user")
    public String showEditUserForm(Model model) {
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
            return "error"; // Redirect to an error page or handle appropriately
        }

        model.addAttribute("user", user);
        return "edit-user"; // Return the Thymeleaf template for editing user info
    }

    @PostMapping("/edit-user")
    public String editUser(@RequestParam Long userId,
            @RequestParam String address,
            @RequestParam String phoneNumber,
            RedirectAttributes redirectAttributes) { // Use RedirectAttributes here
        try {
            User user = userService.findById(userId); // Assume you have this method
            if (user == null) {
                redirectAttributes.addFlashAttribute("message", "User not found");
                redirectAttributes.addFlashAttribute("success", false); // Indicate failure
                return "redirect:/edit-user"; // Redirect back to edit user form
            }

            // Update user details
            user.setAddress(address);
            user.setPhoneNumber(phoneNumber);

            userService.updateUser(user); // Ensure this method exists in your service

            redirectAttributes.addFlashAttribute("message", "User details updated successfully!");
            redirectAttributes.addFlashAttribute("success", true); // Indicate success
            return "redirect:/profile"; // Redirect to profile page after update
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to edit user: " + e.getMessage());
            redirectAttributes.addFlashAttribute("success", false); // Indicate failure
            return "redirect:/edit-user"; // Redirect back to edit user form on error
        }
    }

    @GetMapping("/add-pet")
    public String showAddPetForm(Model model) {
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
            return "error"; // Redirect to an error page or handle appropriately
        }

        model.addAttribute("user", user);
        return "add-pet"; // Return the Thymeleaf template for adding a pet
    }

    @PostMapping("/add-pet")
    public String addPet(
            @RequestParam String petName,
            @RequestParam String petType,
            @RequestParam int petAge,
            @RequestParam String petBio,
            RedirectAttributes redirectAttributes) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = userService.findByUsername(username);
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "User not found");
            redirectAttributes.addFlashAttribute("success", false); // Indicate failure
            return "redirect:/add-pet";
        }

        // Validate pet age
        if (petAge < 0 || petAge > 20) {
            redirectAttributes.addFlashAttribute("message", "Pet age must be between 0 and 20 years");
            redirectAttributes.addFlashAttribute("success", false); // Indicate failure
            return "redirect:/add-pet";
        }

        // Create and register new pet
        Pet newPet = new Pet(petName, petType, petAge, petBio, user);
        user.getPets().add(newPet);
        petService.addPet(newPet);

        redirectAttributes.addFlashAttribute("message", "Pet added successfully!");
        redirectAttributes.addFlashAttribute("success", true); // Indicate success
        return "redirect:/profile"; // Redirect to profile page after adding pet
    }

    @GetMapping("/user-prescriptions")
    public String showUserPrescriptions(Model model) {
        // Get the logged-in user's username
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Fetch the user details using the username
        User user = userService.findByUsername(username);
        if (user == null) {
            model.addAttribute("message", "User not found");
            return "error"; // Redirect to an error page or handle appropriately
        }

        // Fetch the prescriptions for the user
        List<Prescription> prescriptions = prescriptionService.findByUser(user.getId());

        // Add the prescriptions to the model to pass to the view
        model.addAttribute("prescriptions", prescriptions);

        return "profile/user-prescriptions"; // The name of your Thymeleaf template
    }
}