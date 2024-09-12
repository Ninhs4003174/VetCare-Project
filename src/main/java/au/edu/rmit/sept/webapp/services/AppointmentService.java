package au.edu.rmit.sept.webapp.services;

import java.util.Collection;

import au.edu.rmit.sept.webapp.models.Appointment;

public interface AppointmentService {
    public Collection<Appointment> getAppointments();
    public void saveAppointment(Appointment appointment);
}
