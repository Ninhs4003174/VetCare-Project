package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.PrescriptionRequest;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.AppointmentService;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.PrescriptionRequestService;
import au.edu.rmit.sept.webapp.service.PrescriptionService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PrescriptionController {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionController.class);

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final PetService petService;
    private final PrescriptionService prescriptionService;

    @Autowired
    private PrescriptionRequestService prescriptionRequestService;

    @Autowired
    public PrescriptionController(AppointmentService appointmentService, UserService userService,
            PetService petService, PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.petService = petService;
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
            model.addAttribute("userRole", vetUser.getRole());
        } else {
            return "403";
        }
        // Fetch the user details using the username

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
                model.addAttribute("pets", petService.findPetsByUser(client));

                // Example medication and dosage lists
                List<String> medications = Arrays.asList("Medication A", "Medication B", "Medication C");
                List<String> dosages = Arrays.asList("10mg", "20mg", "30mg");

                model.addAttribute("medications", medications);
                model.addAttribute("dosages", dosages);

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

    @PostMapping("/submit-prescription")
    public String submitPrescription(@ModelAttribute Prescription prescription, BindingResult result, Model model,
            @RequestParam Long clientId, @RequestParam Long vetId, @RequestParam String petName) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Please correct the errors in the form.");
            return "vet-dashboard/send-prescription"; // Ensure this matches the template path
        }

        // Set the userId, vetId, and petId in the prescription object
        prescription.setUserId(clientId);
        prescription.setVetId(vetId);
        prescription.setPetName(petName); // Ensure this field exists in the Prescription entity
        prescription.setCreatedAt(LocalDateTime.now());
        prescription.setUpdatedAt(LocalDateTime.now());
        prescription.setDeliveryStatus("PENDING");

        prescriptionService.savePrescription(prescription);

        return "redirect:/patients";
    }

    @GetMapping("/prescriptions")
    public String viewPrescriptions(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User vetUser = userService.findByUsername(username);

        if (vetUser != null && vetUser.getRole() == UserRole.VET) {
            List<Prescription> prescriptions = prescriptionService.findPrescriptionsByVetId(vetUser.getId());
            model.addAttribute("prescriptions", prescriptions);
            return "vet-dashboard/view-prescriptions";
        } else {
            return "403";
        }
    }

    @GetMapping("/view-prescription-requests")
    public String viewPrescriptionRequests(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User vetUser = userService.findByUsername(username);

        if (vetUser != null) {
            List<PrescriptionRequest> prescriptionRequests = prescriptionRequestService.findAll().stream()
                    .filter(request -> {
                        Prescription prescription = prescriptionService.findById(request.getPrescriptionId())
                                .orElse(null);
                        return prescription != null && prescription.getVetId().equals(vetUser.getId());
                    })
                    .collect(Collectors.toList());
            model.addAttribute("prescriptionRequests", prescriptionRequests);
            return "vet-dashboard/view-prescription-requests";
        } else {
            return "403";
        }
    }

    @PostMapping("/approve-prescription-request/{id}")
    public String approvePrescriptionRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            PrescriptionRequest request = prescriptionRequestService.findById(id).orElse(null);
            if (request == null) {
                redirectAttributes.addFlashAttribute("message", "Prescription request not found");
                redirectAttributes.addFlashAttribute("success", false);
                return "redirect:/view-prescription-requests";
            }

            // Fetch the prescription by ID
            Prescription prescription = prescriptionService.findById(request.getPrescriptionId()).orElse(null);
            if (prescription == null) {
                redirectAttributes.addFlashAttribute("message", "Prescription not found");
                redirectAttributes.addFlashAttribute("success", false);
                return "redirect:/view-prescription-requests";
            }

            // Get the authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User vetUser = userService.findByUsername(username);
            // Check if the logged-in vet is the same as the vet who prescribed it
            if (!prescription.getVetId().equals(vetUser.getId())) {
                redirectAttributes.addFlashAttribute("message", "You are not authorized to approve this request");
                redirectAttributes.addFlashAttribute("success", false);
                return "redirect:/view-prescription-requests";
            }

            // Update the prescription request
            request.setStatus("APPROVED");
            request.setProcessedAt(LocalDateTime.now());
            request.setProcessedBy(vetUser);
            prescriptionRequestService.save(request);

            // Update the prescription
            prescription.setRefillsAvailable(prescription.getRefillsAvailable() - 1);
            prescription.setRefillRequest(true);
            prescription.setUpdatedAt(LocalDateTime.now());
            prescriptionService.savePrescription(prescription);

            redirectAttributes.addFlashAttribute("message", "Prescription request approved successfully!");
            redirectAttributes.addFlashAttribute("success", true);
            return "redirect:/view-prescription-requests";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",
                    "Failed to approve prescriptionsss request: " + e.getMessage());
            redirectAttributes.addFlashAttribute("success", false);
            return "redirect:/view-prescription-requests";
        }
    }

}
