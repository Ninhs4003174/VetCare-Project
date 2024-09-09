package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.PetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRecordRepository extends JpaRepository<PetRecord, Long> {
}
