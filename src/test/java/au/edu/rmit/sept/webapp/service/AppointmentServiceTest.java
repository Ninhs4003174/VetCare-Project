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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository repository; // Mocking the AppointmentRepository to avoid hitting the database

    @InjectMocks
    private AppointmentServiceImpl appointmentService; // Injecting the service implementation to test its behavior

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    // Test for fetching all appointments
    @Test
    void testGetAppointments() {
        List<Appointment> appointments = new ArrayList<>(); // Simulating an empty list of appointments
        when(repository.findAll()).thenReturn(appointments); // Mocking the repository call

        Collection<Appointment> result = appointmentService.getAppointments(); // Call the method to test
        assertEquals(appointments, result); // Verify the result matches the mocked data
        verify(repository).findAll(); // Verify that repository.findAll() was called
    }

    // Test for fetching appointments by user
    @Test
    void testGetAppointmentsByUser() {
        User user = new User(); // Create a dummy user
        List<Appointment> appointments = new ArrayList<>(); // Simulate a list of appointments
        when(repository.findByUser(user)).thenReturn(appointments); // Mock the repository method for user-specific appointments

        List<Appointment> result = appointmentService.getAppointmentsByUser(user); // Test method
        assertEquals(appointments, result); // Verify result
        verify(repository).findByUser(user); // Ensure repository call was made
    }

    // Test for fetching appointments by veterinarian ID
    @Test
    void testGetAppointmentsByVet() {
        Long vetId = 1L; // Dummy veterinarian ID
        List<Appointment> appointments = new ArrayList<>(); // Simulate a list of appointments
        when(repository.findByVetId(vetId)).thenReturn(appointments); // Mock the repository method

        List<Appointment> result = appointmentService.getAppointmentsByVet(vetId); // Test method
        assertEquals(appointments, result); // Verify result
        verify(repository).findByVetId(vetId); // Ensure repository call was made
    }

    // Test for saving a valid appointment
    @Test
    void testSaveAppointment_Success() {
        Appointment appointment = new Appointment();
        appointment.setDate(LocalDate.now().plusDays(1).toString()); // Set a valid future date
        appointment.setTime(LocalTime.of(10, 0).toString()); // Set a valid time
    
        // Mock save() method which returns void
        doNothing().when(repository).save(appointment);
    
        appointmentService.saveAppointment(appointment); // Call the method to test
    
        // Verify that save was called
        verify(repository).save(appointment); // Ensure repository.save() was called
    }
    
    // Test for attempting to save an appointment with a past date
    @Test
    void testSaveAppointment_PastDate() {
        Appointment appointment = new Appointment();
        appointment.setDate(LocalDate.now().minusDays(1).toString());  // Past date
        appointment.setTime(LocalTime.of(10, 0).toString()); // Valid time

        // Expect an IllegalArgumentException due to past date
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.saveAppointment(appointment));

        // Verify the exception message
        assertEquals("Cannot book appointments for past dates.", exception.getMessage());
        verify(repository, never()).save(appointment); // Ensure that save() was not called
    }

    // Test for attempting to save an appointment with a past time on the same day
    @Test
    void testSaveAppointment_PastTimeToday() {
        Appointment appointment = new Appointment();
        appointment.setDate(LocalDate.now().toString());  // Today's date
        appointment.setTime(LocalTime.now().minusHours(1).toString());  // Past time

        // Expect an IllegalArgumentException due to past time
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.saveAppointment(appointment));

        // Verify the exception message
        assertEquals("Cannot book appointments for a past time today.", exception.getMessage());
        verify(repository, never()).save(appointment); // Ensure that save() was not called
    }

    // Test for canceling an appointment
    @Test
    void testCancelAppointment() {
        Long appointmentId = 1L; // Dummy appointment ID
        doNothing().when(repository).deleteById(appointmentId); // Mock delete method

        appointmentService.cancelAppointment(appointmentId); // Call the method to test

        verify(repository).deleteById(appointmentId); // Ensure deleteById() was called
    }

    // Test for finding an appointment by ID (success case)
    @Test
    void testFindAppointmentById_Success() {
        Long appointmentId = 1L; // Dummy appointment ID
        Appointment appointment = new Appointment(); // Create a dummy appointment
        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment)); // Mock repository findById

        Appointment result = appointmentService.findAppointmentById(appointmentId); // Call the method to test
        assertEquals(appointment, result); // Verify result
        verify(repository).findById(appointmentId); // Ensure repository.findById() was called
    }

    // Test for finding an appointment by ID (invalid ID)
    @Test
    void testFindAppointmentById_InvalidId() {
        Long appointmentId = 1L; // Dummy appointment ID
        when(repository.findById(appointmentId)).thenReturn(Optional.empty()); // Mock repository findById with empty result

        // Expect an IllegalArgumentException due to invalid ID
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.findAppointmentById(appointmentId));

        // Verify the exception message
        assertEquals("Invalid appointment ID: 1", exception.getMessage());
        verify(repository).findById(appointmentId); // Ensure findById() was called
    }

    // Test for updating a valid appointment
    @Test
    void testUpdateAppointment_Success() {
        Appointment appointment = new Appointment();
        appointment.setDate(LocalDate.now().plusDays(1).toString());  // Future date
        appointment.setTime(LocalTime.of(10, 0).toString()); // Valid time

        appointmentService.updateAppointment(appointment); // Call the method to test

        verify(repository).save(appointment); // Ensure repository.save() was called
    }

    // Test for attempting to update an appointment with a past date
    @Test
    void testUpdateAppointment_PastDate() {
        Appointment appointment = new Appointment();
        appointment.setDate(LocalDate.now().minusDays(1).toString());  // Past date
        appointment.setTime(LocalTime.of(10, 0).toString()); // Valid time

        // Expect an IllegalArgumentException due to past date
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.updateAppointment(appointment));

        // Verify the exception message
        assertEquals("Cannot update appointments for past dates.", exception.getMessage());
        verify(repository, never()).save(appointment); // Ensure that save() was not called
    }

    // Test for updating appointment status (success case)
    @Test
    void testUpdateAppointmentStatus_Success() {
        Long appointmentId = 1L; // Dummy appointment ID
        Appointment appointment = new Appointment(); // Create a dummy appointment
        when(repository.findById(appointmentId)).thenReturn(Optional.of(appointment)); // Mock repository findById

        appointmentService.updateAppointmentStatus(appointmentId, "Completed"); // Call the method to test

        verify(repository).save(appointment); // Ensure save() was called
        assertEquals("Completed", appointment.getStatus()); // Verify the status update
    }

    // Test for updating appointment status with an invalid ID
    @Test
    void testUpdateAppointmentStatus_InvalidId() {
        Long appointmentId = 1L; // Dummy appointment ID
        when(repository.findById(appointmentId)).thenReturn(Optional.empty()); // Mock repository findById with empty result

        // Expect an IllegalArgumentException due to invalid ID
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.updateAppointmentStatus(appointmentId, "Completed"));

        // Verify the exception message
        assertEquals("Invalid appointment ID: 1", exception.getMessage());
        verify(repository, never()).save(any()); // Ensure save() was not called
    }
}
