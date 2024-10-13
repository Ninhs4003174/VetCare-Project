package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class ClinicDashBoardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "receptionist", roles = {"RECEPTIONIST"})
    void testClinicHome() throws Exception {
        mockMvc.perform(get("/clinichome"))
                .andExpect(status().isOk())
                .andExpect(view().name("clinic-dashboard/clinichome"));
    }

    @Test
    @WithMockUser(username = "receptionist", roles = {"RECEPTIONIST"})
    void testVetList() throws Exception {
        mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(view().name("clinic-dashboard/vets"));
    }

    @Test
    @WithMockUser(username = "receptionist", roles = {"RECEPTIONIST"})
    void testAddVetForm() throws Exception {
        mockMvc.perform(get("/clinic-add-vet"))
                .andExpect(status().isOk())
                .andExpect(view().name("clinic-dashboard/add-vet"));
    }

    @Test
    @WithMockUser(username = "receptionist", roles = {"RECEPTIONIST"})
    void testAppointmentList() throws Exception {
        mockMvc.perform(get("/appointmentlist"))
                .andExpect(status().isOk())
                .andExpect(view().name("clinic-dashboard/appointment-list"));
    }

    @Test
    @WithMockUser(username = "receptionist", roles = {"RECEPTIONIST"})
    void testShowClinicInfo() throws Exception {
        mockMvc.perform(get("/clinicinfo"))
                .andExpect(status().isOk())
                .andExpect(view().name("clinic-dashboard/clinic-info"));
    }
}

