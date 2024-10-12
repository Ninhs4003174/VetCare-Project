package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ClinicDashBoardController {

    @Autowired
    private UserService userService;

    @GetMapping("/clinichome")
    public String clinicHome() {
        return "clinic-dashboard/clinichome";
    }

    @GetMapping("/vets")
    public String vetList(Model model, Authentication authentication) {
        User clinic = userService.findByUsername(authentication.getName());
        List<User> vets = userService.getVetsByClinicId(clinic.getId());
        model.addAttribute("users", vets);
        return "clinic-dashboard/vets";
    }

    @GetMapping("/add-clinic-vet")
    public String addAdminForm(Model model) {
        model.addAttribute("user", new User());
        return "clinic-dashboard/add-clinic-vet";
    }

    @PostMapping("/add-clinic-vet")
    public String addAdmin(@ModelAttribute User user) {
        User clinic = userService.findByUsername(user.getUsername());
        user.setRole(UserRole.VET);
        user.setClinicId(clinic.getId());
        userService.saveUser(user);
        return "redirect:/vets";
    }

    @GetMapping("/edit-vet/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        // Find veterinarian by ID
        User veterinarian = userService.findById(id);

        // Check if the user is valid and has the VET role
        if (veterinarian == null || veterinarian.getRole() != UserRole.VET) {
            throw new IllegalArgumentException("Invalid veterinarian ID: " + id);
        }

        // Add veterinarian to the model to pre-fill the form
        model.addAttribute("user", veterinarian);
        return "clinic-dashboard/edit-vet"; // Render the edit form
    }

    @PostMapping("/edit-vet")
    public String editVeterinarian(
            @RequestParam Long id, // Receive ID from the form
            @RequestParam String email,
            @RequestParam String address,
            @RequestParam String phoneNumber,
            RedirectAttributes redirectAttributes) {

        try {
            // Find the veterinarian by ID
            User veterinarian = userService.findById(id);
            if (veterinarian == null || veterinarian.getRole() != UserRole.VET) {
                throw new IllegalArgumentException("Only veterinarians can be updated through this form.");
            }

            // Update veterinarian's details
            veterinarian.setEmail(email);
            veterinarian.setAddress(address);
            veterinarian.setPhoneNumber(phoneNumber);

            // Save the updated user
            userService.updateUser(veterinarian);

            // Add success message
            redirectAttributes.addFlashAttribute("message", "Veterinarian details updated successfully!");
            redirectAttributes.addFlashAttribute("success", true);
        } catch (IllegalArgumentException e) {
            // Handle invalid data or roles
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("success", false);
            return "redirect:/vets"; // Redirect to vets list or an error page
        } catch (Exception e) {
            // Handle other errors
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to edit veterinarian: " + e.getMessage());
            redirectAttributes.addFlashAttribute("success", false);
        }

        // Redirect to the veterinarians list after a successful update
        return "redirect:/vets";
    }

    // delete by id, cuz id's are unique make sure you're on the right table(s),

}