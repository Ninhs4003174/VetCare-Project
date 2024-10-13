package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.AppointmentService;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.PrescriptionService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PrescriptionControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private UserService userService;

    @Mock
    private PetService petService;

    @Mock
    private PrescriptionService prescriptionService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private PrescriptionController prescriptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void testVetHome_WithValidVet() {
        // Arrange
        User vetUser = new User();
        vetUser.setUsername("vet1");
        vetUser.setRole(UserRole.VET);

        when(authentication.getName()).thenReturn("vet1");
        when(userService.findByUsername("vet1")).thenReturn(vetUser);
        when(appointmentService.getAppointmentsByVet(anyLong())).thenReturn(Collections.emptyList());

        // Act
        String result = prescriptionController.vetHome(model);

        // Assert
        assertEquals("vet-dashboard/vethome", result);
        verify(model).addAttribute("appointments", Collections.emptyList());
        verify(model).addAttribute("username", "vet1");
        verify(model).addAttribute("userRole", UserRole.VET);
    }

    @Test
    void testVetHome_WithNonVetUser_ShouldReturn403() {
        // Arrange
        User nonVetUser = new User();
        nonVetUser.setUsername("user1");
        nonVetUser.setRole(UserRole.CLIENT);

        when(authentication.getName()).thenReturn("user1");
        when(userService.findByUsername("user1")).thenReturn(nonVetUser);

        // Act
        String result = prescriptionController.vetHome(model);

        // Assert
        assertEquals("403", result);
    }

    @Test
    void testShowPatientsDashboard_WithValidVet() {
        // Arrange
        User vetUser = new User();
        vetUser.setUsername("vet1");
        vetUser.setRole(UserRole.VET);
        vetUser.setId(1L);

        when(authentication.getName()).thenReturn("vet1");
        when(userService.findByUsername("vet1")).thenReturn(vetUser);
        when(appointmentService.getAppointmentsByVet(anyLong())).thenReturn(Collections.emptyList());
        when(userService.findAllById(anyList())).thenReturn(Collections.emptyList());

        // Act
        String result = prescriptionController.showPatientsDashboard(model);

        // Assert
        assertEquals("vet-dashboard/patients", result);
        verify(model).addAttribute("clients", Collections.emptyList());
    }

    @Test
    void testShowPatientsDashboard_WithNonVetUser_ShouldReturn403() {
        // Arrange
        User nonVetUser = new User();
        nonVetUser.setUsername("user1");
        nonVetUser.setRole(UserRole.CLIENT);

        when(authentication.getName()).thenReturn("user1");
        when(userService.findByUsername("user1")).thenReturn(nonVetUser);

        // Act
        String result = prescriptionController.showPatientsDashboard(model);

        // Assert
        assertEquals("403", result);
    }
    @Test
    void testSubmitPrescription_WithValidData() {
        // Arrange
        Prescription prescription = new Prescription();
        User vetUser = new User();
        vetUser.setId(1L);
    
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(prescriptionService).savePrescription(any(Prescription.class));
    
        // Act
        String result = prescriptionController.submitPrescription(prescription, bindingResult, model, 1L, 1L, "petName");
    
        // Assert
        assertEquals("redirect:/patients", result);
        verify(prescriptionService).savePrescription(any(Prescription.class));
    }
    
    
    @Test
    void testSubmitPrescription_WithBindingErrors() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = prescriptionController.submitPrescription(new Prescription(), bindingResult, model, 1L, 1L, "petName");

        // Assert
        assertEquals("vet-dashboard/send-prescription", result);
        verify(model).addAttribute("errorMessage", "Please correct the errors in the form.");
    }

    @Test
    void testViewPrescriptions_WithValidVet() {
        // Arrange
        User vetUser = new User();
        vetUser.setId(1L);
        vetUser.setRole(UserRole.VET);

        when(authentication.getName()).thenReturn("vet1");
        when(userService.findByUsername("vet1")).thenReturn(vetUser);
        when(prescriptionService.findPrescriptionsByVetId(anyLong())).thenReturn(Collections.emptyList());

        // Act
        String result = prescriptionController.viewPrescriptions(model);

        // Assert
        assertEquals("vet-dashboard/view-prescriptions", result);
        verify(model).addAttribute("prescriptions", Collections.emptyList());
    }

    @Test
    void testViewPrescriptions_WithNonVetUser_ShouldReturn403() {
        // Arrange
        User nonVetUser = new User();
        nonVetUser.setUsername("user1");
        nonVetUser.setRole(UserRole.CLIENT);

        when(authentication.getName()).thenReturn("user1");
        when(userService.findByUsername("user1")).thenReturn(nonVetUser);

        // Act
        String result = prescriptionController.viewPrescriptions(model);

        // Assert
        assertEquals("403", result);
    }
}
