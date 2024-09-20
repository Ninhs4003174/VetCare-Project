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

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        return "signup";
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "home"; // This corresponds to home.html
    }

    @PostMapping("/signup")
    public String registerUser(@RequestParam String username,
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
            if (userService.isUsernameTaken(username)) {
                model.addAttribute("message", "Username is already taken");
                return "signup";
            }
            if (userService.isEmailTaken(email)) {
                model.addAttribute("message", "Email is already taken");
                return "signup";
            }

            // Validate password strength
            if (password.length() < 8) {
                model.addAttribute("message", "Password must be at least 8 characters long");
                return "signup";
            }

            // Register user through UserService
            userService.registerUser(username, email, password);

            // Register pet through UserService
            User user = userService.findUserByEmail(email);
            if (user == null) {
                model.addAttribute("message", "User not found");
                return "signup";
            }

            logger.info("User found: {}", user);

            // Ensure the user's pets set is initialized
            if (user.getPets() == null) {
                user.setPets(new HashSet<>()); // Initialize if null
            }

            Pet newPet = new Pet(petName, petType, petAge, petBio, user);
            logger.info("Creating new pet: {}", newPet);

            user.getPets().add(newPet); // Add pet to user's list
            petService.addPet(newPet); // Save pet to database

            model.addAttribute("message", "User and pet registered successfully!");

        } catch (Exception e) {
            logger.error("Registration failed", e);
            model.addAttribute("message", "Registration failed: " + e.getMessage());
        }

        return "signup";
    }
}