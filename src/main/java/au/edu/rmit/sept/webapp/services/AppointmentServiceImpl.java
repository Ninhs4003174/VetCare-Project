package au.edu.rmit.sept.webapp.services;

import java.time.LocalTime;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.repositories.AppointmentRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Appointment> getAppointments() {
        return repository.findAll();
    }

    @Override
    public void saveAppointment(Appointment appointment) {
        // Validate appointment time
        if (!isValidAppointmentTime(appointment.getTime())) {
            throw new IllegalArgumentException("Appointment time must be between 9 AM and 5 PM, in 15-minute intervals.");
        }

        // Check for overlapping appointments with the same vet
        if (isOverlappingAppointment(appointment)) {
            throw new IllegalArgumentException("This time slot is already booked for the selected vet.");
        }

        repository.save(appointment);
    }

    // Validate if the time is between 9 AM and 5 PM and 15-minute interval
    private boolean isValidAppointmentTime(String time) {
        LocalTime appointmentTime = LocalTime.parse(time);
        LocalTime startTime = LocalTime.of(9, 0);  // 9 AM
        LocalTime endTime = LocalTime.of(17, 0);   // 5 PM

        return (appointmentTime.equals(startTime) || appointmentTime.isAfter(startTime))
                && appointmentTime.isBefore(endTime)
                && appointmentTime.getMinute() % 15 == 0; // Must be a multiple of 15 minutes
    }

    // Check for overlapping appointments for the same vet
    private boolean isOverlappingAppointment(Appointment newAppointment) {
        Collection<Appointment> existingAppointments = repository.findByVetName(newAppointment.getVetName());

        for (Appointment appointment : existingAppointments) {
            LocalTime newTime = LocalTime.parse(newAppointment.getTime());
            LocalTime existingTime = LocalTime.parse(appointment.getTime());

            // Compare appointment times (assuming 15-minute slots)
            if (newTime.equals(existingTime)) {
                return true; // Overlapping appointment found
            }
        }
        return false;
    }

    @Override
    public void cancelAppointment(Long id) {
        repository.deleteById(id);
    }

    public Appointment findAppointmentById(Long id) {
        Appointment appointment = repository.findById(id);
    if (appointment == null) {
        throw new IllegalArgumentException("Invalid appointment ID: " + id);
    }
    return appointment;
}

    @Override
    public void updateAppointment(Appointment appointment) {
    // Validate and check for conflicts similar to saveAppointment
    if (!isValidAppointmentTime(appointment.getTime())) {
        throw new IllegalArgumentException("Appointment time must be between 9 AM and 5 PM, in 15-minute intervals.");
    }

    if (isOverlappingAppointment(appointment)) {
        throw new IllegalArgumentException("This time slot is already booked for the selected vet.");
    }

    repository.save(appointment); // Saves the edited appointment
    }


}
