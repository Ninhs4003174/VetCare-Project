package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Review;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Method to save a new review
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    // Method to get all reviews
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Method to get reviews for a specific user
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUser(user);
    }

    // Method to get reviews by clinic name
    public List<Review> getReviewsByClinicName(String clinicName) {
        return reviewRepository.findByClinicName(clinicName);
    }
}
