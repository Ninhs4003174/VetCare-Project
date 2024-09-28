package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.enums.UserRole; // Import your UserRole enum
import au.edu.rmit.sept.webapp.model.User; // Assuming you have a User model
import au.edu.rmit.sept.webapp.service.UserService; // Import your UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    // Separate login pages for each role (optional)
    @GetMapping("/login-client")
    public String loginClient() {
        return "login-client"; // Return the client login page
    }

    @GetMapping("/login-receptionist")
    public String loginReceptionist() {
        return "login-receptionist"; // Return the receptionist login page
    }

    @GetMapping("/login-veterinarian")
    public String loginVeterinarian() {
        return "login-veterinarian"; // Return the veterinarian login page
    }

    // General login handling (for all roles)
    @PostMapping("/login-client")
    public String handleClientLogin(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        User user = userService.authenticate(username, password); // Actual authentication logic
        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login-client"; // Redirect to general login page on error
        }

        // Fetch user role and redirect based on the role
        UserRole userRole = user.getRole();
        System.out.println("Authenticated User Role: " + userRole); // Log the user role
        switch (userRole) {
            case CLIENT:
                return "redirect:/userhome";
            case RECEPTIONIST:
                return "redirect:/receptionisthome";
            case VET:
                return "redirect:/vethome";
            default:
                model.addAttribute("error", "Unknown role");
                return "login"; // Return general login page if role is unknown
        }
    }

    @GetMapping("/userhome")
public String userHome(Model model) {
    // You can add any attributes you need to the model here
    return "userhome"; // This should match the name of your userhome.html
}
}

