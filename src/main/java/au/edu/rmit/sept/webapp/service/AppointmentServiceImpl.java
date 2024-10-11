package au.edu.rmit.sept.webapp.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.AppointmentRepository;

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
    public List<Appointment> getAppointmentsByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public List<Appointment> getAppointmentsByVet(Long vetId) {
        return repository.findByVetId(vetId);
    }

    @Override
public void saveAppointment(Appointment appointment) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate appointmentDate = LocalDate.parse(appointment.getDate(), formatter);
    LocalDate today = LocalDate.now();

    // Validate that the appointment is not for a past date
    if (appointmentDate.isBefore(today)) {
        throw new IllegalArgumentException("Cannot book appointments for past dates.");
    }

    // If booking for today, ensure the time is not in the past
    if (appointmentDate.equals(today) && LocalTime.parse(appointment.getTime()).isBefore(LocalTime.now())) {
        throw new IllegalArgumentException("Cannot book appointments for a past time today.");
    }

    if (appointment.getId() == null) {
        if (!isValidAppointmentTime(appointment.getTime())) {
            throw new IllegalArgumentException(
                    "Appointment time must be between 9 AM and 5 PM, in 15-minute intervals.");
        }

        if (isOverlappingAppointment(appointment)) {
            throw new IllegalArgumentException("This time slot is already booked for the selected vet.");
        }

        repository.save(appointment);
    } else {
        updateAppointment(appointment);
    }
}

    private boolean isValidAppointmentTime(String time) {
        if (time == null || time.isEmpty()) {
            throw new IllegalArgumentException("Appointment time cannot be null or empty.");
        }

        LocalTime appointmentTime = LocalTime.parse(time);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        return (appointmentTime.equals(startTime) || appointmentTime.isAfter(startTime))
                && appointmentTime.isBefore(endTime)
                && appointmentTime.getMinute() % 15 == 0;
    }

    private boolean isOverlappingAppointment(Appointment newAppointment) {
        String newAppointmentTime = newAppointment.getTime();
        String newAppointmentDate = newAppointment.getDate();

        if (newAppointmentTime == null || newAppointmentTime.isEmpty() || newAppointmentDate == null
                || newAppointmentDate.isEmpty()) {
            throw new IllegalArgumentException("Appointment time and date cannot be null or empty.");
        }

        LocalTime newTime = LocalTime.parse(newAppointmentTime);
        LocalDate newDate = LocalDate.parse(newAppointmentDate);

        Collection<Appointment> existingAppointments = repository.findByVetId(newAppointment.getVetId());

        for (Appointment appointment : existingAppointments) {
            String existingAppointmentTime = appointment.getTime();
            String existingAppointmentDate = appointment.getDate();

            if (existingAppointmentTime == null || existingAppointmentTime.isEmpty() || existingAppointmentDate == null
                    || existingAppointmentDate.isEmpty()) {
                continue;
            }

            LocalTime existingTime = LocalTime.parse(existingAppointmentTime);
            LocalDate existingDate = LocalDate.parse(existingAppointmentDate);

            if (newTime.equals(existingTime) && newDate.equals(existingDate)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void cancelAppointment(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Appointment findAppointmentById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID: " + id));
    }

    @Override
public void updateAppointment(Appointment appointment) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate appointmentDate = LocalDate.parse(appointment.getDate(), formatter);
    LocalDate today = LocalDate.now();

    // Validate that the appointment is not for a past date
    if (appointmentDate.isBefore(today)) {
        throw new IllegalArgumentException("Cannot update appointments for past dates.");
    }

    // If updating for today, ensure the time is not in the past
    if (appointmentDate.equals(today) && LocalTime.parse(appointment.getTime()).isBefore(LocalTime.now())) {
        throw new IllegalArgumentException("Cannot update appointments for a past time today.");
    }

    if (!isValidAppointmentTime(appointment.getTime())) {
        throw new IllegalArgumentException(
                "Appointment time must be between 9 AM and 5 PM, in 15-minute intervals.");
    }

    if (isOverlappingAppointment(appointment)) {
        throw new IllegalArgumentException("This time slot is already booked for the selected vet.");
    }

    repository.save(appointment);
}
    @Override
    public void updateAppointmentStatus(Long appointmentId, String status) {
        // Fetch the appointment by ID
        Appointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID: " + appointmentId));

        // Update the status of the appointment
        appointment.setStatus(status);

        // Save the updated appointment
        repository.save(appointment);
    }

}