package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.Review;
import au.edu.rmit.sept.webapp.repository.AppointmentRepository;
import au.edu.rmit.sept.webapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviewsByVetName(String vetName) {
        List<Appointment> appointments = appointmentRepository.findByVetName(vetName);
        return reviewRepository.findByAppointmentIn(appointments);
    }

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }
}
