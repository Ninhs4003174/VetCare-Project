package au.edu.rmit.sept.webapp.controller;


import java.util.regex.Pattern;
import java.util.stream.Collectors;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ClinicDashBoardController {

    private static final Logger logger = LoggerFactory.getLogger(ClinicDashBoardController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private AppointmentService appointmentService;

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

    @PostMapping("/clinic-add-vet")
    public String addVet(@ModelAttribute User user, Authentication authentication) {
        User clinic = userService.findByUsername(authentication.getName());
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

    @GetMapping("/appointmentlist")
public String appointmentList(Model model, Authentication authentication) {
    // Retrieve the logged-in clinic user
    User clinic = userService.findByUsername(authentication.getName());
    
    // Check if the user is a valid clinic
    if (clinic == null || clinic.getRole() != UserRole.RECEPTIONIST) {
        logger.warn("Clinic user not found or user is not a clinic");
        return "403";  // Access denied page
    }

    // Retrieve vets associated with the clinic
    List<User> vets = userService.getVetsByClinicId(clinic.getId());
    
    List<Appointment> allAppointments = new ArrayList<>();
    for (User vet : vets) {
        // Retrieve appointments for each vet and add them to the list
        allAppointments.addAll(appointmentService.getAppointmentsByVet(vet.getId()));
    }

    // Check if any appointments were found
    if (allAppointments.isEmpty()) {
        model.addAttribute("noAppointmentsMessage", "No appointments with any vets.");
    } else {
        // Log appointment details
        allAppointments.forEach(appointment -> logger.info(
                "Appointment details: ID={}, Pet Name={}, Vet ID={}, User ID={}", 
                appointment.getId(), appointment.getPetName(), appointment.getVetId(), appointment.getUserId()));

        // Add data to the model
        model.addAttribute("vets", vets);
        model.addAttribute("appointments", allAppointments);
        model.addAttribute("username", clinic.getUsername()); // Correctly add the username from the clinic user
    }

    // Return the appointment list view
    return "clinic-dashboard/appointment-list";  // Ensure this matches your template path
}



}