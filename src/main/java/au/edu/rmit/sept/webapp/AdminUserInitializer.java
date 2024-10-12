package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
        String adminEncodedPassword = "admin123";

        // Receptionist user details
        String receptionistUsername = "clinic";
        String receptionistEmail = "clinic@example.com";
        String receptionistEncodedPassword = "clinic123";

        // Vet user details
        String vetUsername = "vetuser";
        String vetEmail = "vet@example.com";
        String vetEncodedPassword = "vet123";
        Long clinicId = 2L; // Assuming clinic with ID 1 exists

        // Check if admin user already exists
        if (!userService.isUsernameTaken(adminUsername)) {
            UserRole adminRole = UserRole.ADMIN;
            userService.registerUser(adminUsername, adminEmail, adminEncodedPassword, adminRole, null);
            System.out.println("Admin user inserted successfully!");
        } else {
            System.out.println("Admin user already exists.");
        }

        // Check if receptionist user already exists
        if (!userService.isUsernameTaken(receptionistUsername)) {
            UserRole receptionistRole = UserRole.RECEPTIONIST;
            userService.registerUser(receptionistUsername, receptionistEmail, receptionistEncodedPassword,
                    receptionistRole, null);
            System.out.println("Receptionist user inserted successfully!");
        } else {
            System.out.println("Receptionist user already exists.");
        }

        // Check if vet user already exists
        if (!userService.isUsernameTaken(vetUsername)) {
            UserRole vetRole = UserRole.VET;
            userService.registerUser(vetUsername, vetEmail, vetEncodedPassword, vetRole, clinicId);
            System.out.println("Vet user inserted successfully!");
        } else {
            System.out.println("Vet user already exists.");
        }
    }
}