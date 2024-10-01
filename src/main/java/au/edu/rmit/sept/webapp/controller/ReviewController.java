package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.Review;
import au.edu.rmit.sept.webapp.repository.AppointmentRepository;
import au.edu.rmit.sept.webapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Display reviews for a particular vet by vet name
    @GetMapping("/vet/{vetName}")
    public String getReviewsByVet(@PathVariable String vetName, Model model) {
        // Find all appointments for the vet and then retrieve reviews for those
        // appointments
        List<Appointment> appointments = appointmentRepository.findByVetName(vetName);
        model.addAttribute("appointments", appointments);

        // Collect reviews associated with those appointments (using appointments' IDs)
        List<Review> reviews = reviewRepository.findByAppointmentIn(appointments); // You can adjust this in
                                                                                   // ReviewRepository

        model.addAttribute("reviews", reviews);
        return "reviews";
    }

    // Display the page to create a new review for a vet based on an appointment
    @GetMapping("/create/{appointmentId}")
    public String createReviewPage(@PathVariable Long appointmentId, Model model) {
        Appointment appointment = appointmentRepository.findById(appointmentId); // Use the provided findById method
        model.addAttribute("appointment", appointment);
        return "createReview";
    }

    // Add a new review for a vet
    @PostMapping
    public String addReview(@ModelAttribute Review review) {
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
        return "redirect:/reviews/vet/" + review.getAppointment().getVetName(); // Redirect to vet's reviews
    }
}
