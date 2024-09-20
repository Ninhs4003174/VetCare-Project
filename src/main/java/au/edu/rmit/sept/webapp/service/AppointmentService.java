package au.edu.rmit.sept.webapp.service;

import java.util.Collection;

import au.edu.rmit.sept.webapp.model.Appointment;

public interface AppointmentService {
    public Collection<Appointment> getAppointments();
    public void saveAppointment(Appointment appointment);
    public void cancelAppointment(Long id);
    public Appointment findAppointmentById(Long id); 
    public void updateAppointment(Appointment appointment);

}

//after editing appointment - new changes must be displayed on the appointments page (list.html), it shows only the old details
