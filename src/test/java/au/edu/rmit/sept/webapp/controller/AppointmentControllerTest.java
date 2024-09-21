// package au.edu.rmit.sept.webapp.controller;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.test.web.servlet.MockMvc;

// import au.edu.rmit.sept.webapp.service.AppointmentService;
// import au.edu.rmit.sept.webapp.service.VetsService;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class AppointmentControllerTest {

//     @Autowired
//     private MockMvc mvc;

//     @MockBean
//     private AppointmentService appointmentService;

//     @MockBean
//     private VetsService vetService;

//     @Test
//     void shouldBookAppointment() throws Exception {
//         // Simulate POST request to book an appointment and check for a redirect
//         mvc.perform(post("/appointments/book")
//                 .param("petName", "Fido")
//                 .param("vetName", "Dr. Smith")
//                 .param("date", "2024-09-18")
//                 .param("time", "9:00 AM")
//                 .param("status", "Scheduled"))
//             .andExpect(status().is3xxRedirection())  // Check if the response is a redirection
//             .andExpect(redirectedUrl("/appointments"));  // Check the redirect URL
//     }

//     @Test
//     void shouldCancelAppointment() throws Exception {
//         // Simulate POST request to cancel an appointment and check for a redirect
//         mvc.perform(post("/appointments/cancel")
//                 .param("id", "1"))
//             .andExpect(status().is3xxRedirection())  // Check if the response is a redirection
//             .andExpect(redirectedUrl("/appointments"));  // Check the redirect URL
//     }
// }
