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
        model.addAttribute("pets", userService.findPetsByUser(user));

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
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User user = userService.findByUsername(username);
            System.out.println("Selected Vet: " + appointment.getVetId());

            if (user == null) {
                throw new IllegalArgumentException("User not found.");
            }
            if (result.hasErrors()) {
                model.addAttribute("vets", vetService.getAllVets());
                model.addAttribute("timeSlots", getTimeSlots());
                return "appointments/book";
            }

            Pet pet = petService.findById(appointment.getPetId());
            if (pet != null) {
                appointment.setPetName(pet.getName());
            } else {
                throw new IllegalArgumentException("Pet not found.");
            }

            System.out.println("Selected Vet: " + appointment.getVetId());
            appointment.setUser(user);
            appointment.setStatus("Scheduled");
            appointmentService.saveAppointment(appointment);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("vets", vetService.getAllVets());
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