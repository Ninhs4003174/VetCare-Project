package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Review;
import au.edu.rmit.sept.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUser(User user);

    List<Review> findByClinicName(String clinicName);
}
