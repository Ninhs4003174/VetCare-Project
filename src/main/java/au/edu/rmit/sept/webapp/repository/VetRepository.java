package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VetRepository extends JpaRepository<Vet, Long> {
}
