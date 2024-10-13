package au.edu.rmit.sept.webapp.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.AppointmentService;
import au.edu.rmit.sept.webapp.service.EmailNotificationService;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.VetBookingService;

class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private VetBookingService vetService;

    @Mock
    private UserService userService;

    @Mock
    private PetService petService;

    @Mock
    private EmailNotificationService emailNotificationService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mocking the security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAllAppointments() {
        String username = "testUser";
        when(authentication.getName()).thenReturn(username);

        User user = new User();
        user.setUsername(username);

        List<Appointment> appointments = new ArrayList<>();
        when(userService.findByUsername(username)).thenReturn(user);
        when(appointmentService.getAppointmentsByUser(user)).thenReturn(appointments);

        String viewName = appointmentController.all(model);
        assertEquals("appointments/list", viewName);

        verify(model).addAttribute(eq("appointmentDetailsList"), anyList());
    }

    @Test
    void testShowBookingForm() {
        String username = "testUser";
        when(authentication.getName()).thenReturn(username);

        User user = new User();
        user.setUsername(username);

        when(userService.findByUsername(username)).thenReturn(user);
        when(vetService.getAllVets()).thenReturn(Collections.emptyList());
        when(petService.findPetsByUser(user)).thenReturn(Collections.emptyList());

        String viewName = appointmentController.showBookingForm(model);
        assertEquals("appointments/book", viewName);

        verify(model).addAttribute(eq("appointment"), any(Appointment.class));
        verify(model).addAttribute(eq("vets"), anyList());
        verify(model).addAttribute(eq("pets"), anyList());
        verify(model).addAttribute(eq("timeSlots"), anyList());
    }

    @Test
    void testBookAppointment_Error_NoPetSelected() {
        String username = "testUser";
        when(authentication.getName()).thenReturn(username);

        User user = new User();
        user.setUsername(username);

        Appointment appointment = new Appointment();

        when(userService.findByUsername(username)).thenReturn(user);

        String viewName = appointmentController.bookAppointment(appointment, bindingResult, model);
        assertEquals("appointments/book", viewName);

        verify(model).addAttribute(eq("errorMessage"), eq("Please select a pet."));
    }

    @Test
    void testCancelAppointment() {
        Long appointmentId = 1L;

        String viewName = appointmentController.cancelAppointment(appointmentId);
        assertEquals("redirect:/appointments", viewName);

        verify(appointmentService).cancelAppointment(appointmentId);
    }

    @Test
    void testShowEditForm() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();

        when(appointmentService.findAppointmentById(appointmentId)).thenReturn(appointment);
        when(vetService.getAllVets()).thenReturn(Collections.emptyList());

        String viewName = appointmentController.showEditForm(appointmentId, model);
        assertEquals("appointments/edit", viewName);

        verify(model).addAttribute(eq("appointment"), eq(appointment));
        verify(model).addAttribute(eq("vets"), anyList());
        verify(model).addAttribute(eq("timeSlots"), anyList());
    }

    @Test
    void testEditAppointment_Success() {
        Appointment appointment = new Appointment();
        appointment.setDate(LocalDate.now().plusDays(1).toString());
        appointment.setTime(LocalTime.now().toString());

        String username = "testUser";
        when(authentication.getName()).thenReturn(username);

        User user = new User();
        user.setUsername(username);

        when(userService.findByUsername(username)).thenReturn(user);

        String viewName = appointmentController.editAppointment(appointment, bindingResult, model);
        assertEquals("redirect:/appointments", viewName);

        verify(appointmentService).updateAppointment(appointment);
    }
 @Test
    void testUpdateAppointmentStatus_Success() {
        Long appointmentId = 1L;
        String status = "Completed";
        String username = "vetUser";

        User vetUser = new User();
        vetUser.setUsername(username);
        vetUser.setRole(UserRole.VET);

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(vetUser);

        String viewName = appointmentController.updateAppointmentStatus(appointmentId, status, model);
        assertEquals("redirect:/vethome", viewName);

        verify(appointmentService).updateAppointmentStatus(appointmentId, status);
    }

    @Test
    void testUpdateAppointmentStatus_UnauthorizedUser() {
        Long appointmentId = 1L;
        String status = "Completed";
        String username = "user";

        User normalUser = new User();
        normalUser.setUsername(username);
        normalUser.setRole(UserRole.CLIENT);  // Not a vet role

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(normalUser);

        String viewName = appointmentController.updateAppointmentStatus(appointmentId, status, model);
        assertEquals("403", viewName);  // Unauthorized access

        verify(appointmentService, never()).updateAppointmentStatus(anyLong(), anyString());
    }

    @Test
void testEditAppointment_Error() {
    // Set up the appointment with a past date
    Appointment appointment = new Appointment();
    appointment.setDate(LocalDate.now().minusDays(1).toString());  // Past date
    appointment.setTime(LocalTime.now().plusHours(1).toString());  // Future time to focus on past date

    // Mock the user to be found and authentication to work
    String username = "testUser";
    when(authentication.getName()).thenReturn(username);
    User user = new User();
    user.setUsername(username);
    when(userService.findByUsername(username)).thenReturn(user);

    // No errors from the BindingResult
    when(bindingResult.hasErrors()).thenReturn(false);

    // Call the method and assert the returned view
    String viewName = appointmentController.editAppointment(appointment, bindingResult, model);
    assertEquals("appointments/edit", viewName);

    // Verify that the correct error message is added to the model
    verify(model).addAttribute(eq("errorMessage"), eq("Cannot update an appointment for a past date."));
    verify(model).addAttribute(eq("vets"), anyList());
    verify(model).addAttribute(eq("timeSlots"), anyList());
}

    @Test
    void testEditAppointment_ValidUpdate() {
        Appointment appointment = new Appointment();
        appointment.setDate(LocalDate.now().plusDays(1).toString());  // Future date
        appointment.setTime(LocalTime.now().toString());

        String username = "testUser";
        when(authentication.getName()).thenReturn(username);

        User user = new User();
        user.setUsername(username);

        when(userService.findByUsername(username)).thenReturn(user);

        String viewName = appointmentController.editAppointment(appointment, bindingResult, model);
        assertEquals("redirect:/appointments", viewName);

        verify(appointmentService).updateAppointment(appointment);
    }
}

