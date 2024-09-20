package au.edu.rmit.sept.webapp.repository;

import java.util.List;

import au.edu.rmit.sept.webapp.model.Appointment;

public interface AppointmentRepository {
    public List<Appointment> findAll();

    public void save(Appointment appointment);
    List<Appointment> findByVetName(String vetName);

    public void deleteById(Long id);
    public Appointment findById(Long id);
}
