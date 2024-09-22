package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.VetBooking;
import au.edu.rmit.sept.webapp.service.AppointmentService;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.VetBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import au.edu.rmit.sept.webapp.model.VetBooking;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AppointmentControllerTest {

    @InjectMocks
    private AppointmentController appointmentController;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private VetBookingService vetBookingService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock authenticated user
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // Set up mock user
        user = new User();
        user.setUsername("testuser");
    }

    @Test
void showBookingForm_shouldReturnBookingViewWithModelAttributes() {
    // Mocking service call to get vets
    List<VetBooking> vets = new ArrayList<>();
    VetBooking vet1 = new VetBooking();
    vet1.setName("Dr. Smith");
    VetBooking vet2 = new VetBooking();
    vet2.setName("Dr. Johnson");
    vets.add(vet1);
    vets.add(vet2);

    when(vetBookingService.getAllVets()).thenReturn(vets);

    // Call the method
    String viewName = appointmentController.showBookingForm(model);

    // Verify model attributes were added
    verify(model, times(1)).addAttribute(eq("appointment"), any(Appointment.class));
    verify(model, times(1)).addAttribute("vets", vets); // Expecting VetBooking objects
    verify(model, times(1)).addAttribute(eq("timeSlots"), any(List.class));
    verify(model, times(1)).addAttribute(eq("today"), any());
    verify(model, times(1)).addAttribute(eq("fiveDaysLater"), any());

    // Check the returned view name
    assertEquals("appointments/book", viewName);
}

@Test
void bookAppointment_success_shouldRedirectToAppointments() {
    // Mock the user and security context
    when(userService.findByUsername("testuser")).thenReturn(user);
    
    // Mock no errors in the form binding
    when(bindingResult.hasErrors()).thenReturn(false);

    // Create a valid appointment
    Appointment appointment = new Appointment();
    appointment.setVetName("Dr. Smith"); // Set a valid vet name or other required fields
    // Populate any other fields necessary for the appointment

    // Mock SecurityContext
    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("testuser");
    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    // Call the method
    String viewName = appointmentController.bookAppointment(appointment, bindingResult, model);

    // Verify the appointment was saved
    verify(appointmentService, times(1)).saveAppointment(appointment);

    // Check redirection
    assertEquals("redirect:/appointments", viewName);
}

    @Test
    void bookAppointment_formHasErrors_shouldReturnBookingViewWithErrors() {
        // Mock form binding errors
        when(bindingResult.hasErrors()).thenReturn(true);

        // Call the method
        Appointment appointment = new Appointment();
        String viewName = appointmentController.bookAppointment(appointment, bindingResult, model);

        // Verify that the appointment was not saved
        verify(appointmentService, never()).saveAppointment(any(Appointment.class));

        // Verify model attributes for error state
        verify(model, times(1)).addAttribute("vets", vetBookingService.getAllVets());
        verify(model, times(1)).addAttribute(eq("timeSlots"), any(List.class));

        // Check the returned view name
        assertEquals("appointments/book", viewName);
    }

    @Test
    void bookAppointment_userNotFound_shouldReturnBookingViewWithErrorMessage() {
        // Mock no user found
        when(userService.findByUsername("testuser")).thenReturn(null);

        Appointment appointment = new Appointment();
        String viewName = appointmentController.bookAppointment(appointment, bindingResult, model);

        // Verify model attributes were added
        verify(model, times(1)).addAttribute("errorMessage", "User not found.");
        verify(model, times(1)).addAttribute("vets", vetBookingService.getAllVets());
        verify(model, times(1)).addAttribute(eq("timeSlots"), any(List.class));

        // Check the returned view name
        assertEquals("appointments/book", viewName);
    }
}
