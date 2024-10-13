package au.edu.rmit.sept.webapp.controller;

import java.util.regex.Pattern;
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

    @GetMapping("/clinic-add-vet")
    public String addVetForm(Model model) {
        model.addAttribute("user", new User());
        return "clinic-dashboard/add-vet";
    }

    // @PostMapping("/clinic-add-vet")
    // public String addVet(@ModelAttribute User user, Authentication authentication) {
    //     User clinic = userService.findByUsername(authentication.getName());
    //     user.setRole(UserRole.VET);
    //     user.setClinicId(clinic.getId());
    //     userService.saveUser(user);
    //     return "redirect:/vets";
    // }

    @PostMapping("/clinic-add-vet")
public String addVet(@ModelAttribute User user, BindingResult result, RedirectAttributes redirectAttributes, Authentication authentication) {
    User clinic = userService.findByUsername(authentication.getName());
    user.setRole(UserRole.VET);
    user.setClinicId(clinic.getId());

    // Validate the user's fields
    if (result.hasErrors()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Please correct the errors in the form.");
        return "redirect:/clinic-add-vet"; // Redirect back to the form
    }

    // Validate email format
    if (!isValidEmail(user.getEmail())) {
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid email format.");
        return "redirect:/clinic-add-vet";
    }

    // Validate phoneNumber format
    if (!isValidPhoneNumber(user.getPhoneNumber())) {
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid phone number format.");
        return "redirect:/clinic-add-vet";
    }

    // Save the user if all validations pass
    userService.saveUser(user);
    redirectAttributes.addFlashAttribute("message", "Veterinarian added successfully!");
    return "redirect:/vets"; // Redirect to the vets list
}

private boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    return email.matches(emailRegex);
}

private boolean isValidPhoneNumber(String phoneNumber) {
    if (phoneNumber == null || phoneNumber.length() != 10) {
        return false;
    }
    return Pattern.matches("\\d{10}", phoneNumber); // Check if it contains only digits
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
        return "clinic-dashboard/edit-vet";  // Render the edit form
    }

    @PostMapping("/edit-vet")
public String editVeterinarian(
    @RequestParam Long id,
    @ModelAttribute User veterinarian,
    RedirectAttributes redirectAttributes) {

    try {
        // Find the veterinarian by ID
        User existingVet = userService.findById(id);
        if (existingVet == null || existingVet.getRole() != UserRole.VET) {
            throw new IllegalArgumentException("Invalid veterinarian ID.");
        }

        // Validate email format
        if (!isValidEmail(veterinarian.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Validate phoneNumber format
    if (!isValidPhoneNumber(veterinarian.getPhoneNumber())) {
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid phone number format.");
        return "redirect:/clinic-add-vet";
    }

        // Update the veterinarian's details
        existingVet.setEmail(veterinarian.getEmail());
        existingVet.setAddress(veterinarian.getAddress());
        existingVet.setPhoneNumber(veterinarian.getPhoneNumber());
        userService.updateUser(existingVet);

        redirectAttributes.addFlashAttribute("message", "Veterinarian details updated successfully!");
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/vets"; // Redirect to vets list or error page
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Failed to edit veterinarian: " + e.getMessage());
    }

    return "redirect:/vets";
}

    
    // @PostMapping("/edit-vet")
    // public String editVeterinarian(
    //     @RequestParam Long id,  // Receive ID from the form
    //     @RequestParam String email,
    //     @RequestParam String address,
    //     @RequestParam String phoneNumber,
    //     RedirectAttributes redirectAttributes) {
    
    //     try {
    //         // Find the veterinarian by ID
    //         User veterinarian = userService.findById(id);
    //         if (veterinarian == null || veterinarian.getRole() != UserRole.VET) {
    //             throw new IllegalArgumentException("Only veterinarians can be updated through this form.");
    //         }
    
    //         // Update veterinarian's details
    //         veterinarian.setEmail(email);
    //         veterinarian.setAddress(address);
    //         veterinarian.setPhoneNumber(phoneNumber);
    
    //         // Save the updated user
    //         userService.updateUser(veterinarian);  
    
    //         // Add success message
    //         redirectAttributes.addFlashAttribute("message", "Veterinarian details updated successfully!");
    //         redirectAttributes.addFlashAttribute("success", true);
    //     } catch (IllegalArgumentException e) {
    //         // Handle invalid data or roles
    //         redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
    //         redirectAttributes.addFlashAttribute("success", false);
    //         return "redirect:/vets";  // Redirect to vets list or an error page
    //     } catch (Exception e) {
    //         // Handle other errors
    //         redirectAttributes.addFlashAttribute("errorMessage", "Failed to edit veterinarian: " + e.getMessage());
    //         redirectAttributes.addFlashAttribute("success", false);
    //     }
    
    //     // Redirect to the veterinarians list after a successful update
    //     return "redirect:/vets";
    // }

//delete  by id, cuz id's are unique  make sure you're on the right table(s), 

}