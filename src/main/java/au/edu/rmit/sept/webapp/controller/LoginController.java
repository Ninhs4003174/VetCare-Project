package au.edu.rmit.sept.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Return the login page
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, Model model) {
        // Basic error handling for missing or incorrect credentials
        if (username.isEmpty() || password.isEmpty()) {
            model.addAttribute("error", "Username and password must be provided");
            return "login"; // Show the error on the login page
        }

        // Simulate login logic (replace with real authentication)
        if (!username.equals("correctUser") || !password.equals("correctPass")) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        // On successful login, redirect to the user home page
        return "redirect:/userhome";
    }

    @GetMapping("/userhome")
    public String userhome() {
        return "userhome";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    // Example of handling exceptions at the controller level
    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception ex) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "error"; // Return error page
    }
}
