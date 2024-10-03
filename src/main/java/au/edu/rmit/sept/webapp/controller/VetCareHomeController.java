package au.edu.rmit.sept.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VetCareHomeController {

    @GetMapping("/vetcaresystemhome")
    public String showHome() {
        return "vetcaresystemhome"; // Serve the role selection view
    }

    @PostMapping("/vetcaresystemhome/selectrole")
    public String handleRoleSelection(@RequestParam("role") String role) {
        System.out.println("Selected role: " + role); // Add this line for debugging
        // Redirect to the appropriate login page based on the role
        switch (role) {
            case "CLIENT":
                return "redirect:/login-client";
            case "RECEPTIONIST":
                return "redirect:/login-receptionist";
            case "VETERINARIAN":
                return "redirect:/login-veterinarian";
            default:
                // Handle unknown roles by redirecting back to the role selection page
                return "redirect:/vetcaresystemhome";
        }
    }
}