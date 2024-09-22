package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.PrescriptionRequest;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.service.PrescriptionRequestService;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.PetService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PrescriptionController {

    @Autowired
    private PrescriptionRequestService prescriptionRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @GetMapping("/prescription-management")
    public String showRequestFormAndPrescriptions(Model model) {
        // Get the logged-in user's username
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Fetch the current user and their pets
        User currentUser = userService.findByUsername(username);
        List<Pet> pets = petService.findPetsByUser(currentUser); // Get user's pets

        // Fetch prescription requests for the current user
        List<PrescriptionRequest> pendingRequests = prescriptionRequestService.findByUserId(currentUser.getId());

        // Add pets and pending requests to the model
        model.addAttribute("pets", pets);
        model.addAttribute("pendingRequests", pendingRequests);
        System.out.println("Pending Requests: " + pendingRequests);

        return "prescription_management"; // Renders the HTML page
    }

    @PostMapping("/prescription-management/request") // Specify the URL for POST request
    public String requestPrescription(@RequestParam String medication,
            @RequestParam String reason,
            @RequestParam Long petId) {
        // Get the logged-in user's username
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Fetch the user using the username
        User currentUser = userService.findByUsername(username);

        // Fetch the pet by ID
        Pet pet = petService.findPetById(petId);
        if (pet == null) {
            return "redirect:/prescription-management?error=PetNotFound"; // Handle error appropriately
        }

        // Create a new prescription request with the current date
        PrescriptionRequest request = new PrescriptionRequest(currentUser, pet, medication, reason);
        request.setRequestDate(LocalDateTime.now()); // Set the current date and time

        // Save the prescription request to the database
        prescriptionRequestService.saveRequest(request);

        return "redirect:/prescription-management"; // Redirect to the request form
    }
}
