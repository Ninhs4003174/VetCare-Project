package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login-client")
    public String login() {
        return "login-client";
    }

    @PostMapping("/login-client")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        User user = userService.authenticate(username, password);
        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login-client";
        }

        UserRole userRole = user.getRole();
        switch (userRole) {
            case CLIENT:
                return "redirect:/userhome";
            case RECEPTIONIST:
                return "redirect:/clinichome";
            case VET:
                return "redirect:/appointments/vethome";
            case ADMIN:
                return "redirect:/adminhome";
            default:
                model.addAttribute("error", "Unknown role");
                return "login-client";
        }
    }

    @GetMapping("/userhome")
    public ModelAndView userHome() {
        if (!hasRole("CLIENT")) {
            return new ModelAndView("403");
        }
        return new ModelAndView("userhome");
    }

    @GetMapping("/receptionisthome")
    public ModelAndView receptionistHome() {
        if (!hasRole("RECEPTIONIST")) {
            return new ModelAndView("403");
        }
        return new ModelAndView("clinichome");
    }

    @GetMapping("/adminhome")
    public ModelAndView adminHome() {
        if (!hasRole("ADMIN")) {
            return new ModelAndView("403");
        }
        return new ModelAndView("admin-dashboard/adminhome");
    }

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