package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.PrescriptionRequest;
import au.edu.rmit.sept.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import au.edu.rmit.sept.webapp.model.PrescriptionRequest;

import java.util.List;

@Repository
public interface PrescriptionRequestRepository extends JpaRepository<PrescriptionRequest, Long> {
    List<PrescriptionRequest> findByUserId(Long userId);

}
