package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Prescription;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByVetId(Long vetId);
}