package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // Updated email regex for standard email validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    // Regex to check if password contains at least one number (and can optionally
    // include special characters)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9]).{8,}$"); // Modify this regex to
                                                                                           // enforce special characters
                                                                                           // if needed

    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String petName,
            @RequestParam String petType,
            @RequestParam int petAge,
            @RequestParam String petBio,
            Model model) {
        try {
            // Validate email structure
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                model.addAttribute("message", "Invalid email format");
                return "signup";
            }

            // Ensure username length does not exceed 20 characters
            if (username.length() > 20) {
                model.addAttribute("message", "Username must not exceed 20 characters");
                return "signup";
            }

            // Check if username is taken
            if (userService.isUsernameTaken(username)) {
                model.addAttribute("message", "Username is already taken");
                return "signup";
            }

            // Check if email is taken
            if (userService.isEmailTaken(email)) {
                model.addAttribute("message", "Email is already taken");
                return "signup";
            }

            // Validate password strength (at least 8 characters, with a number)
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                model.addAttribute("message",
                        "Password must be at least 8 characters long and contain at least one number");
                return "signup";
            }

            // Validate pet age (limit between 0 and 20 years)
            if (petAge < 0 || petAge > 20) {
                model.addAttribute("message", "Pet age must be between 0 and 20 years");
                return "signup";
            }

            // Register the user
            userService.registerUser(username, email, password);

            // Find the newly registered user by email
            User user = userService.findUserByEmail(email);
            if (user == null) {
                model.addAttribute("message", "User not found");
                return "signup";
            }

            logger.info("User found: {}", user);

            // Initialize user's pets set if null
            if (user.getPets() == null) {
                user.setPets(new HashSet<>());
            }

            // Create and register new pet
            Pet newPet = new Pet(petName, petType, petAge, petBio, user);
            logger.info("Creating new pet: {}", newPet);

            user.getPets().add(newPet); // Add pet to user's list
            petService.addPet(newPet); // Save pet to database

            model.addAttribute("message", "User and pet registered successfully!");
            return "redirect:/login"; // Redirect to login after successful signup

        } catch (Exception e) {
            logger.error("Registration failed", e);
            model.addAttribute("message", "Registration failed: " + e.getMessage());
            return "signup";
        }
    }
}
