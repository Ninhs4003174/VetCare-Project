package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.AppointmentService;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.VetBookingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private VetBookingService vetService;

    @MockBean
    private UserService userService;

    @MockBean
    private PetService petService;

    @Test
    @WithMockUser(username = "testuser", roles = {"CLIENT"})
    void testGetAllAppointments() throws Exception {
        // Setup mock data
        User user = new User();
        user.setUsername("testuser");

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDate(LocalDate.now().plusDays(1).toString());
        appointment.setPetName("Buddy");

        when(userService.findByUsername("testuser")).thenReturn(user);
        when(appointmentService.getAppointmentsByUser(user)).thenReturn(Collections.singletonList(appointment));

        // Execute GET request
        ResultActions result = mockMvc.perform(get("/appointments"));

        // Verify response and model attributes
        result.andExpect(status().isOk())
              .andExpect(view().name("appointments/list"))
              .andExpect(model().attributeExists("appointmentDetailsList"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"CLIENT"})
    void testShowBookingForm() throws Exception {
        // Setup mock data
        User user = new User();
        user.setUsername("testuser");

        when(userService.findByUsername("testuser")).thenReturn(user);
        when(petService.findPetsByUser(user)).thenReturn(Collections.emptyList());
        when(vetService.getAllVets()).thenReturn(Collections.emptyList());

        // Execute GET request
        ResultActions result = mockMvc.perform(get("/appointments/book"));

        // Verify response and model attributes
        result.andExpect(status().isOk())
              .andExpect(view().name("appointments/book"))
              .andExpect(model().attributeExists("appointment", "vets", "pets", "timeSlots", "today", "fiveDaysLater"));
    }


    
    @Test
    @WithMockUser(username = "vetuser", roles = {"VET"})
    void testUpdateAppointmentStatusSuccess() throws Exception {
        // Setup mock data
        User vetUser = new User();
        vetUser.setUsername("vetuser");
        vetUser.setRole(UserRole.VET);

        when(userService.findByUsername("vetuser")).thenReturn(vetUser);

        // Execute POST request
        ResultActions result = mockMvc.perform(post("/appointments/updateStatus")
                .param("appointmentId", "1")
                .param("status", "COMPLETED"));

        // Verify redirection
        result.andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrl("/vethome"));

        // Verify service interaction
        Mockito.verify(appointmentService, Mockito.times(1)).updateAppointmentStatus(1L, "COMPLETED");
    }

  


}
