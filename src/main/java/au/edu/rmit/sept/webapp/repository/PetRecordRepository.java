package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.PetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRecordRepository extends JpaRepository<PetRecord, Long> {
    // Find pet records by the user ID
    List<PetRecord> findByUserId(Long userId);
}
