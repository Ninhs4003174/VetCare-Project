package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        String username = "admin3";
        String email = "admin@example.com";
        String rawPassword = "admin123";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = "admin123";

        // Check if admin user already exists
        if (!userService.isUsernameTaken(username)) {
            UserRole adminRole = UserRole.ADMIN; // Fixed role for admin signup
            userService.registerUser(username, email, encodedPassword, adminRole);
            System.out.println("Admin user inserted successfully!");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}