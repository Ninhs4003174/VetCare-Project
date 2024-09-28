package au.edu.rmit.sept.webapp.service;

import java.util.Collection;
import java.util.List;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;

public interface AppointmentService {
    Collection<Appointment> getAppointments();
    // method to retrieve appointments for the logged-in user
    List<Appointment> getAppointmentsByUser(User user); 
    //method to save appointments
    void saveAppointment(Appointment appointment);
     // method to cancel appointments
    void cancelAppointment(Long id);
    //finding apointment byid to update it
    Appointment findAppointmentById(Long id);
    void updateAppointment(Appointment appointment);
}



