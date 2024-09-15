package au.edu.rmit.sept.webapp.services;

import java.util.Collection;

import au.edu.rmit.sept.webapp.models.Appointment;

public interface AppointmentService {
    public Collection<Appointment> getAppointments();
    public void saveAppointment(Appointment appointment);
    public void cancelAppointment(Long id);
    public Appointment findAppointmentById(Long id); 
    public void updateAppointment(Appointment appointment);

}
