package au.edu.rmit.sept.webapp.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.services.AppointmentService;
import au.edu.rmit.sept.webapp.services.VetService;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final VetService vetService;
    
    @Autowired
    public AppointmentController(AppointmentService appointmentService, VetService vetService) {
        this.appointmentService = appointmentService;
        this.vetService = vetService;
    }

    @GetMapping
    public String all(Model model) {
        Collection<Appointment> appointments = appointmentService.getAppointments();
        model.addAttribute("appointments", appointments);
        return "appointments/list";
    }

    @GetMapping("/book")
    public String showBookingForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("vets", vetService.getAllVets());  // Pass list of vets to the form

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
        if (result.hasErrors()) {
            model.addAttribute("vets", vetService.getAllVets());
            model.addAttribute("timeSlots", getTimeSlots());  // Add time slots on error
            return "appointments/book";
        }

        try {
            appointmentService.saveAppointment(appointment);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("vets", vetService.getAllVets());
            model.addAttribute("timeSlots", getTimeSlots());  // Add time slots on error
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
    Appointment appointment = appointmentService.findAppointmentById(id); // Add a method to get the appointment by ID
    model.addAttribute("appointment", appointment);
    model.addAttribute("vets", vetService.getAllVets()); // Pass the list of vets
    return "appointments/edit"; // Return the edit form
}

@PostMapping("/edit")
public String editAppointment(@ModelAttribute Appointment appointment, BindingResult result, Model model) {
    if (result.hasErrors()) {
        model.addAttribute("vets", vetService.getAllVets());
        return "appointments/edit";
    }

    try {
        appointmentService.updateAppointment(appointment); // Call the update method in your service
    } catch (IllegalArgumentException e) {
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("vets", vetService.getAllVets());
        return "appointments/edit";
    }

    return "redirect:/appointments"; // Redirect to the list after successful update
}


}
