package au.edu.rmit.sept.webapp.service;

import java.util.Collection;
import java.util.List;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;

public interface AppointmentService {
    Collection<Appointment> getAppointments();

    List<Appointment> getAppointmentsByUser(User user);

    void saveAppointment(Appointment appointment);

    void cancelAppointment(Long id);

    Appointment findAppointmentById(Long id);

    void updateAppointment(Appointment appointment);

    List<Appointment> getAppointmentsByVet(Long vetId);
}