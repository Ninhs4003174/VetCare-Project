package au.edu.rmit.sept.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import au.edu.rmit.sept.webapp.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
