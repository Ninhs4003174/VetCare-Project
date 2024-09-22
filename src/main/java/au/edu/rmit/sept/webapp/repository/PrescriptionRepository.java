package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByRefillStatus(String status);
}
