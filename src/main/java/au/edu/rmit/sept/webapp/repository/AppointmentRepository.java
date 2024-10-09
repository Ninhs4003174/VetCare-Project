package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {
    void save(Appointment appointment);

    List<Appointment> findAll();

    List<Appointment> findByVetId(Long vetId);

    List<Appointment> findByUser(User user);

    void deleteById(Long id);

    Optional<Appointment> findById(Long id); // Updated to return Optional<Appointment>
}