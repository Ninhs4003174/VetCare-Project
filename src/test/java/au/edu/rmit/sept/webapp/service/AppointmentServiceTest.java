package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository repository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(); // Populate user if necessary
        user.setUsername("testuser");
    }

    @Test
    void saveAppointment_validAppointment_shouldSave() {
        Appointment appointment = new Appointment();
        appointment.setId(null); // New appointment
        appointment.setVetName("Dr. Smith");
        appointment.setDate(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        appointment.setTime("09:00");

        appointmentService.saveAppointment(appointment);

        verify(repository, times(1)).save(appointment);
    }

    @Test
    void saveAppointment_pastDate_shouldThrowException() {
        Appointment appointment = new Appointment();
        appointment.setDate(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        appointment.setTime("09:00");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.saveAppointment(appointment);
        });

        assertEquals("Cannot book appointments for past dates.", thrown.getMessage());
    }

    @Test
    void saveAppointment_invalidTime_shouldThrowException() {
        Appointment appointment = new Appointment();
        appointment.setDate(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        appointment.setTime("08:00"); // Invalid time

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.saveAppointment(appointment);
        });

        assertEquals("Appointment time must be between 9 AM and 5 PM, in 15-minute intervals.", thrown.getMessage());
    }

    @Test
    void saveAppointment_overlappingAppointment_shouldThrowException() {
        Appointment existingAppointment = new Appointment();
        existingAppointment.setDate(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        existingAppointment.setTime("10:00");
        existingAppointment.setVetName("Dr. Smith");

        when(repository.findByVetName("Dr. Smith")).thenReturn(Arrays.asList(existingAppointment));

        Appointment newAppointment = new Appointment();
        newAppointment.setDate(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        newAppointment.setTime("10:00"); // Overlapping time
        newAppointment.setVetName("Dr. Smith");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.saveAppointment(newAppointment);
        });

        assertEquals("This time slot is already booked for the selected vet.", thrown.getMessage());
    }
}
