package au.edu.rmit.sept.webapp.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.AppointmentService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class ClinicDashBoardControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private Authentication authentication;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ClinicDashBoardController controller;

    private User clinicUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock a clinic user
        clinicUser = new User();
        clinicUser.setId(1L);
        clinicUser.setUsername("clinicUser");
        clinicUser.setRole(UserRole.RECEPTIONIST);
    }

    @Test
    void clinicHome_shouldReturnClinicHomeView() {
        String viewName = controller.clinicHome();
        assertEquals("clinic-dashboard/clinichome", viewName);
    }

    @Test
    void vetList_shouldAddVetsToModelAndReturnVetsView() {
        when(authentication.getName()).thenReturn("clinicUser");
        when(userService.findByUsername("clinicUser")).thenReturn(clinicUser);

        List<User> vets = new ArrayList<>();
        vets.add(new User());
        when(userService.getVetsByClinicId(1L)).thenReturn(vets);

        String viewName = controller.vetList(model, authentication);

        verify(model).addAttribute("users", vets);
        assertEquals("clinic-dashboard/vets", viewName);
    }

    @Test
    void addVetForm_shouldAddNewUserToModelAndReturnAddVetView() {
        String viewName = controller.addVetForm(model);

        verify(model).addAttribute(eq("user"), any(User.class));
        assertEquals("clinic-dashboard/add-vet", viewName);
    }

    @Test
    void addVet_shouldSaveUserAndRedirectToVets() {
        when(authentication.getName()).thenReturn("clinicUser");
        when(userService.findByUsername("clinicUser")).thenReturn(clinicUser);

        User newVet = new User();
        newVet.setUsername("newVet");

        String viewName = controller.addVet(newVet, authentication);

        verify(userService).saveUser(newVet);
        assertEquals(UserRole.VET, newVet.getRole());
        assertEquals(clinicUser.getId(), newVet.getClinicId());
        assertEquals("redirect:/vets", viewName);
    }

    @Test
    void showEditForm_shouldAddVetToModelAndReturnEditVetView() {
        User vet = new User();
        vet.setId(2L);
        vet.setRole(UserRole.VET);

        when(userService.findById(2L)).thenReturn(vet);

        String viewName = controller.showEditForm(2L, model);

        verify(model).addAttribute("user", vet);
        assertEquals("clinic-dashboard/edit-vet", viewName);
    }

    @Test
    void showEditForm_shouldThrowExceptionIfInvalidVetId() {
        when(userService.findById(999L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> controller.showEditForm(999L, model)
        );

        assertEquals("Invalid veterinarian ID: 999", exception.getMessage());
    }

    @Test
    void editVeterinarian_shouldUpdateVetAndRedirectToVets() {
        User vet = new User();
        vet.setId(2L);
        vet.setRole(UserRole.VET);

        when(userService.findById(2L)).thenReturn(vet);

        String viewName = controller.editVeterinarian(
            2L, "vet@example.com", "123 Street", "1234567890", redirectAttributes);

        verify(userService).updateUser(vet);
        assertEquals("vet@example.com", vet.getEmail());
        assertEquals("123 Street", vet.getAddress());
        assertEquals("1234567890", vet.getPhoneNumber());
        assertEquals("redirect:/vets", viewName);
    }

    @Test
    void appointmentList_shouldAddAppointmentsToModelAndReturnListView() {
        when(authentication.getName()).thenReturn("clinicUser");
        when(userService.findByUsername("clinicUser")).thenReturn(clinicUser);

        List<User> vets = new ArrayList<>();
        vets.add(new User());

        when(userService.getVetsByClinicId(clinicUser.getId())).thenReturn(vets);

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());

        when(appointmentService.getAppointmentsByVet(anyLong())).thenReturn(appointments);

        String viewName = controller.appointmentList(model, authentication);

        verify(model).addAttribute(eq("appointments"), anyList());
        verify(model).addAttribute("vets", vets);
        assertEquals("clinic-dashboard/appointment-list", viewName);
    }

    @Test
    void listPetNames_shouldAddPetNamesToModel() {
        when(authentication.getName()).thenReturn("clinicUser");
        when(userService.findByUsername("clinicUser")).thenReturn(clinicUser);

        List<User> vets = new ArrayList<>();
        vets.add(new User());
        when(userService.getVetsByClinicId(1L)).thenReturn(vets);

        Appointment appointment = new Appointment();
        appointment.setPetName("Buddy");

        when(appointmentService.getAppointmentsByVet(anyLong())).thenReturn(List.of(appointment));

        String viewName = controller.listPetNames(model, authentication);

        verify(model).addAttribute("petNames", Set.of("Buddy"));
        assertEquals("clinic-dashboard/patients", viewName);
    }

    @Test
    void showClinicInfo_shouldAddClinicInfoToModelAndReturnView() {
        String viewName = controller.showClinicInfo(model);

        verify(model).addAttribute("clinicName", "Happy Tails Veterinary Clinic");
        verify(model).addAttribute("clinicAddress", "1234 Bark Street, Pawsville, 56789");
        verify(model).addAttribute("contactNumber", "+61 123 456 789");
        assertEquals("clinic-dashboard/clinic-info", viewName);
    }
}
