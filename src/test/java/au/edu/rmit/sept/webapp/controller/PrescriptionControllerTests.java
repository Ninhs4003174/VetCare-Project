package au.edu.rmit.sept.webapp.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.PrescriptionRequest;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.AppointmentService;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.PrescriptionRequestService;
import au.edu.rmit.sept.webapp.service.PrescriptionService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

@WebMvcTest(PrescriptionController.class)
public class PrescriptionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private UserService userService;

    @MockBean
    private PetService petService;

    @MockBean
    private PrescriptionService prescriptionService;

    @MockBean
    private PrescriptionRequestService prescriptionRequestService;

    private User vetUser;

    @BeforeEach
    public void setUp() {
        vetUser = new User();
        vetUser.setId(1L);
        vetUser.setUsername("vet1");
        vetUser.setRole(UserRole.VET);
    }

    @Test
    @WithMockUser(username = "vet1", roles = { "VET" })
    public void testVetHome_WithValidVet() throws Exception {
        when(userService.findByUsername("vet1")).thenReturn(vetUser);
        when(appointmentService.getAppointmentsByVet(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/vethome"))
                .andExpect(status().isOk())
                .andExpect(view().name("vet-dashboard/vethome"))
                .andExpect(model().attributeExists("appointments"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("userRole"));

        verify(userService, times(1)).findByUsername("vet1");
        verify(appointmentService, times(1)).getAppointmentsByVet(anyLong());
    }

    @Test
    @WithMockUser(username = "vet1", roles = { "VET" })
    public void testShowPatientsDashboard_WithValidVet() throws Exception {
        when(userService.findByUsername("vet1")).thenReturn(vetUser);
        when(appointmentService.getAppointmentsByVet(anyLong())).thenReturn(Collections.emptyList());
        when(userService.findAllById(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(view().name("vet-dashboard/patients"))
                .andExpect(model().attributeExists("clients"))
                .andExpect(model().attributeExists("username"));

        verify(userService, times(1)).findByUsername("vet1");
        verify(appointmentService, times(1)).getAppointmentsByVet(anyLong());
        verify(userService, times(1)).findAllById(anyList());
    }

    @Test
    @WithMockUser(username = "vet1", roles = { "VET" })
    public void testSendPrescription_WithValidVetAndClient() throws Exception {
        User client = new User();
        client.setId(1L);

        when(userService.findByUsername("vet1")).thenReturn(vetUser);
        when(userService.findById(1L)).thenReturn(client);
        when(petService.findPetsByUser(client)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/send-prescription").param("clientId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("vet-dashboard/send-prescription"))
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attributeExists("vetUser"))
                .andExpect(model().attributeExists("pets"));

        verify(userService, times(1)).findByUsername("vet1");
        verify(userService, times(1)).findById(1L);
        verify(petService, times(1)).findPetsByUser(client);
    }

    @Test
    @WithMockUser(username = "vet1", roles = { "VET" })
    public void testViewPrescriptions_WithValidVet() throws Exception {
        when(userService.findByUsername("vet1")).thenReturn(vetUser);
        when(prescriptionService.findPrescriptionsByVetId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/prescriptions"))
                .andExpect(status().isOk())
                .andExpect(view().name("vet-dashboard/view-prescriptions"))
                .andExpect(model().attributeExists("prescriptions"));

        verify(userService, times(1)).findByUsername("vet1");
        verify(prescriptionService, times(1)).findPrescriptionsByVetId(anyLong());
    }

    @Test
    @WithMockUser(username = "vet1", roles = { "VET" })
    public void testViewPrescriptionRequests_WithValidVet() throws Exception {
        PrescriptionRequest request = new PrescriptionRequest();
        request.setPrescriptionId(1L);

        Prescription prescription = new Prescription();
        prescription.setId(1L);
        prescription.setVetId(1L);

        when(userService.findByUsername("vet1")).thenReturn(vetUser);
        when(prescriptionRequestService.findAll()).thenReturn(Collections.singletonList(request));
        when(prescriptionService.findById(1L)).thenReturn(Optional.of(prescription));

        mockMvc.perform(get("/view-prescription-requests"))
                .andExpect(status().isOk())
                .andExpect(view().name("vet-dashboard/view-prescription-requests"))
                .andExpect(model().attributeExists("prescriptionRequests"));

        verify(userService, times(1)).findByUsername("vet1");
        verify(prescriptionRequestService, times(1)).findAll();
        verify(prescriptionService, times(1)).findById(1L);
    }
}