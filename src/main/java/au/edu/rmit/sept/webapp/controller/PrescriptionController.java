package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.AppointmentService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PrescriptionController {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionController.class);

    private final AppointmentService appointmentService;
    private final UserService userService;

    @Autowired
    public PrescriptionController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    @GetMapping("/vethome")
    public String vetHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User vetUser = userService.findByUsername(username);

        if (vetUser != null && vetUser.getRole() == UserRole.VET) {
            List<Appointment> vetAppointments = appointmentService.getAppointmentsByVet(vetUser.getId());
            model.addAttribute("appointments", vetAppointments);
            model.addAttribute("username", username);
        } else {
            return "403";
        }

        return "vet-dashboard/vethome"; // Ensure this matches the template path
    }

    @GetMapping("/patients")
    public String showPatientsDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        logger.info("Fetching patients for vet: {}", username);
        User vetUser = userService.findByUsername(username);

        if (vetUser != null && vetUser.getRole() == UserRole.VET) {
            logger.info("Vet user found: {}", vetUser);
            // Fetch appointments for the logged-in vet
            List<Appointment> vetAppointments = appointmentService.getAppointmentsByVet(vetUser.getId());
            logger.info("Number of appointments found: {}", vetAppointments.size());

            // Log detailed appointment information
            vetAppointments.forEach(appointment -> logger.info(
                    "Appointment details: ID={}, User ID={}, Pet ID={}, Pet Name={}",
                    appointment.getId(), appointment.getUserId(), appointment.getPetId(), appointment.getPetName()));

            // Extract unique client IDs from these appointments
            List<Long> clientIds = vetAppointments.stream()
                    .map(Appointment::getUserId)
                    .distinct()
                    .collect(Collectors.toList());
            logger.info("Unique client IDs extracted: {}", clientIds);

            // Fetch client details using these IDs
            List<User> clients = userService.findAllById(clientIds);
            logger.info("Number of clients found: {}", clients.size());

            // Log client details
            clients.forEach(client -> logger.info("Client details: ID={}, Username={}, Email={}",
                    client.getId(), client.getUsername(), client.getEmail()));

            model.addAttribute("clients", clients);
            model.addAttribute("username", username);
        } else {
            logger.warn("Vet user not found or user is not a vet");
            return "403";
        }

        return "vet-dashboard/patients"; // Ensure this matches the template path
    }

    @GetMapping("/send-prescription")
    public String sendPrescription(@RequestParam Long clientId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User vetUser = userService.findByUsername(username);

        if (vetUser != null && vetUser.getRole() == UserRole.VET) {
            User client = userService.findById(clientId);
            if (client != null) {
                model.addAttribute("client", client);
                model.addAttribute("vetUser", vetUser);
                return "vet-dashboard/send-prescription"; // Ensure this matches the template path
            } else {
                logger.warn("Client not found with ID: {}", clientId);
                return "404";
            }
        } else {
            logger.warn("Vet user not found or user is not a vet");
            return "403";
        }
    }
}