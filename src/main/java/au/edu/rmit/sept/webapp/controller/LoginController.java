package au.edu.rmit.sept.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        System.out.println("Navigating to login page");
        return "login";
    }

    @GetMapping("/welcome")
    public String welcome() {
        System.out.println("Navigating to welcome page");
        return "welcome";
    }

    @GetMapping("/userhome")
    public String userhome() {
        System.out.println("Navigating to logged in home page");
        return "userhome";
    }

    // Add mappings for other endpoints as needed
}
