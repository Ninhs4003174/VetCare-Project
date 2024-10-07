package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Review;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.ReviewService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    // Display the page to create a new review (no clinic name passed)
    @GetMapping("/create")
    public String createReviewPage(Model model) {
        model.addAttribute("review", new Review()); // Prepare a new empty review object
        return "create-review"; // Refers to create-review.html template
    }

    // Method to handle the form submission for creating a review
    @PostMapping
    public String addReview(@ModelAttribute Review review, Principal principal) {
        User user = userService.findByUsername(principal.getName()); // Find the logged-in user
        review.setUser(user); // Set the review's author
        review.setReviewDate(LocalDate.now()); // Set the review date to today
        review.setCreatedAt(LocalDateTime.now()); // Set the timestamp when the review was created
        reviewService.saveReview(review); // Save the review to the database
        return "redirect:/reviews"; // Redirect to the main reviews page
    }

    // Method to display all reviews by the logged-in user
    @GetMapping
    public String getUserReviews(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Review> userReviews = reviewService.getReviewsByUser(user); // Fetch reviews written by the user
        model.addAttribute("userReviews", userReviews);
        return "reviews"; // Assuming this page will show user-specific reviews
    }
}
