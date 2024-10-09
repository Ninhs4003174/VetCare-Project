package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.VetBooking;
import au.edu.rmit.sept.webapp.service.AppointmentService;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.VetBookingService;
import au.edu.rmit.sept.webapp.model.VetBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    public AppointmentController(AppointmentService appointmentService, VetBookingService vetService,
            UserService userService) {
        this.appointmentService = appointmentService;
        this.vetService = vetService;
        this.userService = userService;
    }

    @GetMapping
    public String all(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        List<Appointment> appointments = appointmentService.getAppointmentsByUser(user);

        // Create a list to hold appointment details along with vet name
        List<Map<String, Object>> appointmentDetailsList = new ArrayList<>();

        for (Appointment appointment : appointments) {
            Map<String, Object> appointmentDetails = new HashMap<>();
            appointmentDetails.put("appointment", appointment);

            // Fetch the vet details using vetId from the appointment
            VetBooking vet = vetService.getAllVets().stream()
                    .filter(v -> v.getId().equals(appointment.getVetId())) // Match based on vetId
                    .findFirst()
                    .orElse(null);

            if (vet != null) {
                System.out.println("Vet Name: " + vet.getVetName()); // Debugging line
                appointmentDetails.put("vetName", vet.getVetName()); // Add vet name to the details
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
        model.addAttribute("appointment", new Appointment(1L, "Bella", 1L, "2024-09-15", "10:00 AM", "Scheduled"));
        model.addAttribute("vets", vetService.getAllVets());// Pass list of vets to the form
        // Generate time slots and add them to the model
        List<String> timeSlots = getTimeSlots();
        model.addAttribute("timeSlots", timeSlots);
        // Pass today's date and five days later for the date picker
        LocalDate today = LocalDate.now();
        LocalDate fiveDaysLater = today.plusDays(5);
        model.addAttribute("today", today);
        model.addAttribute("fiveDaysLater", fiveDaysLater);

        return "appointments/book";
    }

    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute Appointment appointment, BindingResult result, Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User user = userService.findByUsername(username);
            System.out.println("Selected Vet: " + appointment.getVetId()); // Debugging line

            if (user == null) {
                throw new IllegalArgumentException("User not found.");
            }
            if (result.hasErrors()) {
                model.addAttribute("vets", vetService.getAllVets());
                model.addAttribute("timeSlots", getTimeSlots()); // Add time slots on error
                return "appointments/book";
            }
            System.out.println("Selected Vet: " + appointment.getVetId());
            appointment.setUser(user); // Make sure user is set in the appointment
            appointmentService.saveAppointment(appointment);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("vets", vetService.getAllVets());
            model.addAttribute("timeSlots", getTimeSlots());
            return "appointments/book";
        }
        return "redirect:/appointments";
    }

    // Helper method to generate time slots from 9 AM to 5 PM
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
        Appointment appointment = appointmentService.findAppointmentById(id); // Get the appointment by ID
        model.addAttribute("appointment", appointment);
        model.addAttribute("vets", vetService.getAllVets()); // Pass the list of vets

        // Generate time slots and add them to the model
        List<String> timeSlots = getTimeSlots();
        model.addAttribute("timeSlots", timeSlots);

        return "appointments/edit"; // Return the edit form
    }

    @PostMapping("/edit")
    public String editAppointment(@ModelAttribute Appointment appointment, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("vets", vetService.getAllVets());
            model.addAttribute("timeSlots", getTimeSlots()); // Add time slots on error
            return "appointments/edit";
        }

        try {
            // Retrieve the logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new IllegalArgumentException("User not found.");
            }

            // Set the user in the appointment before updating
            appointment.setUser(user);

            // Proceed with updating the appointment
            appointmentService.updateAppointment(appointment);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("vets", vetService.getAllVets());
            model.addAttribute("timeSlots", getTimeSlots()); // Add time slots on error
            return "appointments/edit";
        }

        return "redirect:/appointments"; // Redirect to the list after successful update
    }

    @GetMapping("/compare-providers")
    public String compareProviders(
            @RequestParam(value = "serviceType", required = false) String serviceType,
            @RequestParam(value = "location", required = false) String location,
            Model model) {
        try {
            // Fetch the filtered vets based on serviceType and location
            List<VetBooking> filteredVets = vetService.getFilteredVets(serviceType, location);

            // Add the list of filtered vets to the model
            model.addAttribute("filteredVets", filteredVets);
        } catch (Exception e) {
            // Handle errors (such as database connection issues)
            model.addAttribute("errorMessage", "Unable to load comparison data due to a server error.");
        }

        // Pass filter criteria back to the view so the user sees what they selected
        model.addAttribute("serviceType", serviceType);
        model.addAttribute("location", location);

        return "appointments/compare-providers"; // New view for comparing providers
    }

}
