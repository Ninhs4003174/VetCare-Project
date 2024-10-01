package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import au.edu.rmit.sept.webapp.model.Appointment;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Fetch reviews based on a list of appointments (this will allow flexibility to
    // filter by vet name)
    List<Review> findByAppointmentIn(List<Appointment> appointments);
}
