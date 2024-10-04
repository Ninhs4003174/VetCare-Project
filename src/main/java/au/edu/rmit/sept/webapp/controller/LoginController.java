package au.edu.rmit.sept.webapp.controller;

import java.util.Collection; // Correct import for java.util.Collection
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    // Combined login page for both clients and receptionists
    @GetMapping("/login-client")
    public String login() {
        return "login-client"; // The same login page for both roles
    }

    // General login handling for both roles (Client and Receptionist)
    @PostMapping("/login-client")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        User user = userService.authenticate(username, password); // Actual authentication logic
        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login-client"; // Redirect to the same login page on error
        }

        // Fetch user role and redirect based on the role
        UserRole userRole = user.getRole();
        switch (userRole) {
            case CLIENT:
                return "redirect:/userhome";
            case RECEPTIONIST:
                return "redirect:/receptionisthome";
            case VET:
                return "redirect:/vethome";
            default:
                model.addAttribute("error", "Unknown role");
                return "login-client"; // Return the login page if the role is unknown
        }
    }

    @GetMapping("/userhome")
    public ModelAndView userHome() {
        if (!hasRole("CLIENT")) {
            return new ModelAndView("403"); // Redirect to access denied page if not CLIENT
        }
        return new ModelAndView("userhome");
    }

    @GetMapping("/receptionisthome")
    public ModelAndView receptionistHome() {
        if (!hasRole("RECEPTIONIST")) {
            return new ModelAndView("403"); // Redirect to access denied page if not RECEPTIONIST
        }
        return new ModelAndView("receptionisthome");
    }

    @GetMapping("/adminhome")
    public ModelAndView adminHome() {
        if (!hasRole("ADMIN")) {
            return new ModelAndView("403"); // Redirect to access denied page if not RECEPTIONIST
        }
        return new ModelAndView("admin-dashboard/adminhome");
    }

    // Method to check role of the logged-in user
    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }
}
