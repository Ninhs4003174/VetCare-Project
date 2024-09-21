package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfile(Model model) {
        // Get the logged-in user's username
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Fetch the user details using the username
        User user = userService.findByUsername(username);

        // If user is null, handle gracefully
        if (user == null) {
            return "error"; // Return an error page or handle the error appropriately
        }

        // Add the user to the model to pass to the view
        model.addAttribute("user", user);

        return "profile"; // The name of your Thymeleaf template
    }

    @GetMapping("/edit-user")
    public String showEditUserForm(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = userService.findByUsername(username);
        if (user == null) {
            return "error";
        }

        model.addAttribute("user", user);
        return "edit-user"; // Return the Thymeleaf template for editing user info
    }

    @PostMapping("/edit-user")
    public String editUser(@RequestParam Long userId,
            @RequestParam String address,
            @RequestParam String phoneNumber,
            Model model) {
        try {
            User user = userService.findById(userId); // Assume you have this method
            if (user == null) {
                model.addAttribute("message", "User not found");
                return "error"; // Handle appropriately
            }

            // Update user details
            user.setAddress(address);
            user.setPhoneNumber(phoneNumber);

            userService.updateUser(user); // Ensure this method exists in your service

            return "redirect:/profile"; // Redirect to profile page after update
        } catch (Exception e) {
            model.addAttribute("message", "Failed to edit user: " + e.getMessage());
            return "edit-user"; // Return to the edit user form on error
        }
    }

}
