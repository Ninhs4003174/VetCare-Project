package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;
import java.util.List;

public interface AppointmentRepository {
    void save(Appointment appointment);
    List<Appointment> findAll();
    List<Appointment> findByVetName(String vetName);
    List<Appointment> findByUser(User user);  // Added method to find appointments by user
   // void save(Appointment appointment);
    void deleteById(Long id);
    Appointment findById(Long id);
}
