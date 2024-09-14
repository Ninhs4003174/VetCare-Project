package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.regex.Pattern;

@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password,
            Model model) {
        try {
            // Validate email structure
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                model.addAttribute("message", "Invalid email format");
                return "signup";
            }

            // Validate password strength
            if (password.length() < 8) {
                model.addAttribute("message", "Password must be at least 8 characters long");
                return "signup";
            }

            // Register user through UserService
            userService.registerUser(username, email, password);
            model.addAttribute("message", "User registered successfully!");

        } catch (Exception e) {
            model.addAttribute("message", "Registration failed: " + e.getMessage());
        }
        return "signup";
    }
}
