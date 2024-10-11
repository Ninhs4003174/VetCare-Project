package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.regex.Pattern;

@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9]).{8,}$");
    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    // Display the signup form (no role selection required, defaults to CLIENT)
    @GetMapping("/signup-client")
    public String showSignupForm(Model model) {
        return "signup-client"; // Return the client-specific signup view
    }

    // Handle the client signup form submission
    @PostMapping("/signup-client")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String petName,
            @RequestParam String petType,
            @RequestParam int petAge,
            @RequestParam String petBio,
            RedirectAttributes redirectAttributes) {

        try {
            // Validate email structure
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                redirectAttributes.addFlashAttribute("message", "Invalid email format");
                redirectAttributes.addFlashAttribute("success", false); // Indicate failure
                return "redirect:/signup-client";
            }

            // Ensure username length does not exceed 20 characters
            if (username.length() > 20) {
                redirectAttributes.addFlashAttribute("message", "Username must not exceed 20 characters");
                redirectAttributes.addFlashAttribute("success", false); // Indicate failure
                return "redirect:/signup-client";
            }

            // Check if username is taken
            if (userService.isUsernameTaken(username)) {
                redirectAttributes.addFlashAttribute("message", "Username is already taken");
                redirectAttributes.addFlashAttribute("success", false); // Indicate failure
                return "redirect:/signup-client";
            }

            // Check if email is taken
            if (userService.isEmailTaken(email)) {
                redirectAttributes.addFlashAttribute("message", "Email is already taken");
                redirectAttributes.addFlashAttribute("success", false); // Indicate failure
                return "redirect:/signup-client";
            }

            // Validate password strength
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                redirectAttributes.addFlashAttribute("message",
                        "Password must be at least 8 characters long and contain at least one number");
                redirectAttributes.addFlashAttribute("success", false); // Indicate failure
                return "redirect:/signup-client";
            }

            // Validate pet age
            if (petAge < 0 || petAge > 20) {
                redirectAttributes.addFlashAttribute("message", "Pet age must be between 0 and 20 years");
                redirectAttributes.addFlashAttribute("success", false); // Indicate failure
                return "redirect:/signup-client";
            }

            // Register the user as CLIENT (no role selection needed)
            UserRole clientRole = UserRole.CLIENT; // Fixed role for client signup
            userService.registerUser(username, email, password, clientRole); // Register user with CLIENT role

            // Find the newly registered user by email
            User user = userService.findUserByEmail(email);
            if (user == null) {
                redirectAttributes.addFlashAttribute("message", "User not found");
                redirectAttributes.addFlashAttribute("success", false); // Indicate failure
                return "redirect:/signup-client";
            }

            logger.info("User found: {}", user);

            // Initialize user's pets set if null
            if (user.getPets() == null) {
                user.setPets(new HashSet<>());
            }

            // Create and register new pet
            Pet newPet = new Pet(petName, petType, petAge, petBio, user);
            logger.info("Creating new pet: {}", newPet);

            user.getPets().add(newPet);
            petService.addPet(newPet);

            // Add confirmation message before redirecting to the client login page
            redirectAttributes.addFlashAttribute("message", "User and pet registered successfully!");
            redirectAttributes.addFlashAttribute("success", true); // Indicate success
            return "redirect:/login-client";

        } catch (Exception e) {
            logger.error("Registration failed", e);
            redirectAttributes.addFlashAttribute("message", "Registration failed: " + e.getMessage());
            redirectAttributes.addFlashAttribute("success", false); // Indicate failure
            return "redirect:/signup-client";
        }
    }
}