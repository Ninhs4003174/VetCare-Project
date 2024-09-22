package au.edu.rmit.sept.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import au.edu.rmit.sept.webapp.model.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
