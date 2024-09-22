package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VetRepository extends JpaRepository<Vet, Long> {
    List<Vet> findByClinicNameContainingIgnoreCase(String query);
}
