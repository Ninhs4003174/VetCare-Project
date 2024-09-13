package au.edu.rmit.sept.webapp.repositories;

import java.util.List;

import au.edu.rmit.sept.webapp.models.Appointment;

public interface AppointmentRepository {
    public List<Appointment> findAll();

    public void save(Appointment appointment);
    List<Appointment> findByVetName(String vetName);

    public void deleteById(Long id);
 
}
