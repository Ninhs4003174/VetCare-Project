package au.edu.rmit.sept.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.model.Resource;
import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    // Find all resources with the specified status (e.g., APPROVED or PENDING)
    List<Resource> findByStatus(String status);
}
