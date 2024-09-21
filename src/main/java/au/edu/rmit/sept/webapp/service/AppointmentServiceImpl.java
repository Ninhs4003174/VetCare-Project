package au.edu.rmit.sept.webapp.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
//import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.Appointment;
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
        return repository.findByUser(user); // Corrected repository call
    }

    @Override
    public void saveAppointment(Appointment appointment) {
       // Convert the String date to LocalDate
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Ensure this matches your date format
    LocalDate appointmentDate = LocalDate.parse(appointment.getDate(), formatter);

    // Prevent booking for past dates
    if (appointmentDate.isBefore(LocalDate.now())) {
        throw new IllegalArgumentException("Cannot book appointments for past dates.");
    }
        // If the appointment is new (id is null), validate and save as a new record
        if (appointment.getId() == null) {
            if (!isValidAppointmentTime(appointment.getTime())) {
                throw new IllegalArgumentException("Appointment time must be between 9 AM and 5 PM, in 15-minute intervals.");
            }

            if (isOverlappingAppointment(appointment)) {
                throw new IllegalArgumentException("This time slot is already booked for the selected vet.");
            }

            repository.save(appointment); // Save as a new appointment
        } else {
            updateAppointment(appointment); // Update the existing appointment
        }
    }

    // Validate if the time is between 9 AM and 5 PM and in 15-minute intervals
    private boolean isValidAppointmentTime(String time) {
        if (time == null || time.isEmpty()) {
            throw new IllegalArgumentException("Appointment time cannot be null or empty.");
        }

        LocalTime appointmentTime = LocalTime.parse(time);
        LocalTime startTime = LocalTime.of(9, 0);  // 9 AM
        LocalTime endTime = LocalTime.of(17, 0);   // 5 PM

        return (appointmentTime.equals(startTime) || appointmentTime.isAfter(startTime))
                && appointmentTime.isBefore(endTime)
                && appointmentTime.getMinute() % 15 == 0; // Must be a multiple of 15 minutes
    }
    private boolean isOverlappingAppointment(Appointment newAppointment) {
        String newAppointmentTime = newAppointment.getTime();
        String newAppointmentDate = newAppointment.getDate(); // Add date to comparison
    
        if (newAppointmentTime == null || newAppointmentTime.isEmpty() || newAppointmentDate == null || newAppointmentDate.isEmpty()) {
            throw new IllegalArgumentException("Appointment time and date cannot be null or empty.");
        }
    
        LocalTime newTime = LocalTime.parse(newAppointmentTime);
        LocalDate newDate = LocalDate.parse(newAppointmentDate); // Parse the date
    
        // Retrieve all appointments for the selected vet
        Collection<Appointment> existingAppointments = repository.findByVetName(newAppointment.getVetName());
    
        // Iterate through the existing appointments and check for overlaps
        for (Appointment appointment : existingAppointments) {
            String existingAppointmentTime = appointment.getTime();
            String existingAppointmentDate = appointment.getDate();
    
            if (existingAppointmentTime == null || existingAppointmentTime.isEmpty() || existingAppointmentDate == null || existingAppointmentDate.isEmpty()) {
                continue; // Skip invalid appointment times or dates
            }
    
            LocalTime existingTime = LocalTime.parse(existingAppointmentTime);
            LocalDate existingDate = LocalDate.parse(existingAppointmentDate);
    
            // Compare both time and date for overlap
            if (newTime.equals(existingTime) && newDate.equals(existingDate)) {
                return true; // Overlapping appointment found (same date and time)
            }
        }
    
        return false; // No overlap found
    }
    
    
    // // Check for overlapping appointments for the same vet
    // private boolean isOverlappingAppointment(Appointment newAppointment) {
    //     String newAppointmentTime = newAppointment.getTime();
    //     if (newAppointmentTime == null || newAppointmentTime.isEmpty()) {
    //         throw new IllegalArgumentException("Appointment time cannot be null or empty.");
    //     }

    //     LocalTime newTime = LocalTime.parse(newAppointmentTime);
    //     Collection<Appointment> existingAppointments = repository.findByVetName(newAppointment.getVetName());

    //     for (Appointment appointment : existingAppointments) {
    //         String existingAppointmentTime = appointment.getTime();
    //         if (existingAppointmentTime == null || existingAppointmentTime.isEmpty()) {
    //             continue; // Skip invalid appointment times
    //         }

    //         LocalTime existingTime = LocalTime.parse(existingAppointmentTime);

    //         // Compare appointment times (assuming 15-minute slots)
    //         if (newTime.equals(existingTime)) {
    //             return true; // Overlapping appointment found
    //         }
    //     }
    //     return false;
    // }
    


    @Override
    public void cancelAppointment(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Appointment findAppointmentById(Long id) {
        Appointment appointment = repository.findById(id);
        if (appointment == null) {
            throw new IllegalArgumentException("Invalid appointment ID: " + id);
        }
        return appointment;
    }

    @Override
    public void updateAppointment(Appointment appointment) {
        // Validate date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate appointmentDate = LocalDate.parse(appointment.getDate(), formatter);
    
        // Prevent booking for past dates
        if (appointmentDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot update appointments to past dates.");
        }
    
        // Validate time and check for conflicts
        if (!isValidAppointmentTime(appointment.getTime())) {
            throw new IllegalArgumentException("Appointment time must be between 9 AM and 5 PM, in 15-minute intervals.");
        }
    
        if (isOverlappingAppointment(appointment)) {
            throw new IllegalArgumentException("This time slot is already booked for the selected vet.");
        }
    
        repository.save(appointment); // Saves the edited appointment
    }
}
