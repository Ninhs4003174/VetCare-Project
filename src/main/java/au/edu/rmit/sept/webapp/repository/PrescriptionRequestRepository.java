package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.PrescriptionRequest;
import au.edu.rmit.sept.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRequestRepository extends JpaRepository<PrescriptionRequest, Long> {
    List<PrescriptionRequest> findByUser(User user);

    List<PrescriptionRequest> findByUserId(Long userId);
}
