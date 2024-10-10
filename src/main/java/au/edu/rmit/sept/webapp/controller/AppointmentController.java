package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.VetBooking;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.AppointmentService;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.VetBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final VetBookingService vetService;
    private final UserService userService;
    private final PetService petService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, VetBookingService vetService,
            UserService userService, PetService petService) {
        this.appointmentService = appointmentService;
        this.vetService = vetService;
        this.userService = userService;
        this.petService = petService;
    }

    @GetMapping
    public String all(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        List<Appointment> appointments = appointmentService.getAppointmentsByUser(user);

        List<Map<String, Object>> appointmentDetailsList = new ArrayList<>();

        for (Appointment appointment : appointments) {
            Map<String, Object> appointmentDetails = new HashMap<>();
            appointmentDetails.put("appointment", appointment);

            VetBooking vet = vetService.getAllVets().stream()
                    .filter(v -> v.getVetUserId().equals(appointment.getVetId()))
                    .findFirst()
                    .orElse(null);

            if (vet != null) {
                System.out.println("Vet Name: " + vet.getVetName());
                appointmentDetails.put("vetName", vet.getVetName());
            } else {
                System.out.println("Vet not found for Vet ID: " + appointment.getVetId());
            }

            appointmentDetailsList.add(appointmentDetails);
        }

        model.addAttribute("appointmentDetailsList", appointmentDetailsList);
        return "appointments/list";
    }

    @GetMapping("/book")
    public String showBookingForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);
    
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("vets", vetService.getAllVets());
        model.addAttribute("pets", petService.findPetsByUser(user)); // Fetch pets from PetService
    
        List<String> timeSlots = getTimeSlots();
        model.addAttribute("timeSlots", timeSlots);
    
        LocalDate today = LocalDate.now();
        LocalDate fiveDaysLater = today.plusDays(5);
        model.addAttribute("today", today);
        model.addAttribute("fiveDaysLater", fiveDaysLater);
    
        return "appointments/book";
    }
    
   
    @PostMapping("/book")
public String bookAppointment(@ModelAttribute Appointment appointment, BindingResult result, Model model) {
    // Declare the user variable outside the try block
    User user = null;

    try {
        // Get the authenticated user's username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Find the user by username
        user = userService.findByUsername(username);
        
        // Ensure user is not null
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        // Check if there are validation errors
        if (result.hasErrors()) {
            model.addAttribute("vets", vetService.getAllVets());
            model.addAttribute("pets", petService.findPetsByUser(user)); // Ensure pets are loaded again
            model.addAttribute("timeSlots", getTimeSlots());
            return "appointments/book";
        }

        // Validate that a pet is selected
        if (appointment.getPetId() == null) {
            throw new IllegalArgumentException("Please select a pet.");
        }
        // Ensure time is not empty
        if (appointment.getTime() == null || appointment.getTime().isEmpty()) {
            throw new IllegalArgumentException("Please select a valid time.");
        }

        // Log the pet ID for debugging
        System.out.println("Pet ID: " + appointment.getPetId());

        // Fetch the pet and set the pet name in the appointment
        Pet pet = petService.findById(appointment.getPetId());
        if (pet != null) {
            appointment.setPetName(pet.getName());
        } else {
            throw new IllegalArgumentException("Pet not found.");
        }

        // Validate appointment date and time
        LocalDate today = LocalDate.now();
        LocalDate appointmentDate = LocalDate.parse(appointment.getDate());
        if (appointmentDate.isBefore(today)) {
            throw new IllegalArgumentException("Cannot book an appointment for a past date.");
        }
        if (appointmentDate.equals(today) && LocalTime.parse(appointment.getTime()).isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("Cannot book an appointment for a past time today.");
        }

        // Set user and status, then save the appointment
        appointment.setUser(user);
        appointment.setStatus("Scheduled");
        appointmentService.saveAppointment(appointment);

    } catch (IllegalArgumentException e) {
        // Handle errors and re-populate the form fields
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("vets", vetService.getAllVets());
        
        // Use the user variable outside the try block
        model.addAttribute("pets", petService.findPetsByUser(user)); 
        model.addAttribute("timeSlots", getTimeSlots());
        return "appointments/book";
    }

    return "redirect:/appointments";
}

    
    private List<String> getTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        while (startTime.isBefore(endTime)) {
            timeSlots.add(startTime.toString());
            startTime = startTime.plusMinutes(15);
        }
        return timeSlots;
    }

    @PostMapping("/cancel")
    public String cancelAppointment(@ModelAttribute("id") Long id) {
        appointmentService.cancelAppointment(id);
        return "redirect:/appointments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Appointment appointment = appointmentService.findAppointmentById(id);
        model.addAttribute("appointment", appointment);
        model.addAttribute("vets", vetService.getAllVets());

        List<String> timeSlots = getTimeSlots();
        model.addAttribute("timeSlots", timeSlots);

        return "appointments/edit";
    }

    @PostMapping("/edit")
    public String editAppointment(@ModelAttribute Appointment appointment, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("vets", vetService.getAllVets());
            model.addAttribute("timeSlots", getTimeSlots());
            return "appointments/edit";
        }

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new IllegalArgumentException("User not found.");
            }

            // Validate that the appointment is not for a past date or past time
            LocalDate today = LocalDate.now();
            LocalDate appointmentDate = LocalDate.parse(appointment.getDate());
            if (appointmentDate.isBefore(today)) {
                throw new IllegalArgumentException("Cannot update an appointment for a past date.");
            }

            // If updating for today, ensure the time is not in the past
            if (appointmentDate.equals(today) && LocalTime.parse(appointment.getTime()).isBefore(LocalTime.now())) {
                throw new IllegalArgumentException("Cannot update an appointment for a past time today.");
            }

            // Set the status to "Scheduled" during updates
            appointment.setStatus("Scheduled");

            appointment.setUser(user);
            appointmentService.updateAppointment(appointment);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("vets", vetService.getAllVets());
            model.addAttribute("timeSlots", getTimeSlots());
            return "appointments/edit";
        }

        return "redirect:/appointments";
    }
    @GetMapping("/compare-providers")
    public String compareProviders(
            @RequestParam(value = "serviceType", required = false) String serviceType,
            @RequestParam(value = "location", required = false) String location,
            Model model) {
        try {
            List<VetBooking> filteredVets = vetService.getFilteredVets(serviceType, location);
            model.addAttribute("filteredVets", filteredVets);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Unable to load comparison data due to a server error.");
        }

        model.addAttribute("serviceType", serviceType);
        model.addAttribute("location", location);

        return "appointments/compare-providers";
    }
    @PostMapping("/updateStatus")
    public String updateAppointmentStatus(
        @RequestParam Long appointmentId, 
        @RequestParam String status, 
        Model model) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User vetUser = userService.findByUsername(username);
    
        if (vetUser != null && vetUser.getRole() == UserRole.VET) {
            // Use the new service method to update the status
            appointmentService.updateAppointmentStatus(appointmentId, status);
    
            return "redirect:/vethome"; // Redirect back to vet's dashboard after update
        } else {
            return "403"; // Unauthorized access
        }
    }
    
} 

