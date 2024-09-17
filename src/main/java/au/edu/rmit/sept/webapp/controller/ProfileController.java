package au.edu.rmit.sept.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;

@Controller
public class ProfileController {
    @GetMapping("/profile")
    public String showProfilePage() {
        // Add attributes to model if needed, e.g., user details
        return "profile"; // This should match the name of your Thymeleaf template
    }
}
