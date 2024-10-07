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
        // Admin user details
        String adminUsername = "admin3";
        String adminEmail = "admin@example.com";
        String adminRawPassword = "admin123";

        String adminEncodedPassword = "admin123";
        // Receptionist user details
        String receptionistUsername = "clinic";
        String receptionistEmail = "clinic@example.com";
        String receptionistEncodedPassword = "clinic123";

        // Check if admin user already exists
        if (!userService.isUsernameTaken(adminUsername)) {
            UserRole adminRole = UserRole.ADMIN;
            userService.registerUser(adminUsername, adminEmail, adminEncodedPassword, adminRole);
            System.out.println("Admin user inserted successfully!");
        } else {
            System.out.println("Admin user already exists.");
        }

        // Check if receptionist user already exists
        if (!userService.isUsernameTaken(receptionistUsername)) {
            UserRole receptionistRole = UserRole.RECEPTIONIST;
            userService.registerUser(receptionistUsername, receptionistEmail, receptionistEncodedPassword,
                    receptionistRole);
            System.out.println("Receptionist user inserted successfully!");
        } else {
            System.out.println("Receptionist user already exists.");
        }
    }
}