package au.edu.rmit.sept.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.model.PetRecord;

@Repository
public interface PetRecordRepository extends JpaRepository<PetRecord, Long> {
}
