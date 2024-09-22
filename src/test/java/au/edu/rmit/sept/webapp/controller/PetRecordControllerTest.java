package au.edu.rmit.sept.webapp.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.any;

import au.edu.rmit.sept.webapp.model.PetRecord;
import au.edu.rmit.sept.webapp.service.PetRecordService;

@SpringBootTest
@AutoConfigureMockMvc
public class PetRecordControllerTest {

        private static String URL = "/records";

        @Autowired
        private MockMvc mvc;

        @MockBean
        private PetRecordService petRecordService;

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" })
        void shouldDisplayPetRecordsTitle() throws Exception {
                mvc.perform(get(URL))
                                .andExpect(status().isOk())
                                .andExpect(content().string(containsString("Pet Records")));
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" })
        void shouldListPetRecords() throws Exception {

                // Mock data as List
                List<PetRecord> petRecords = List.of(
                                new PetRecord("Fluffy", "Golden Retriever", "2015-06-01", null, null, null, null, null,
                                                null, null, null, "Dr. Smith"),
                                new PetRecord("Whiskers", "Persian Cat", "2018-02-15", null, null, null, null, null,
                                                null, null, null, "Dr. Brown"));

                // Mock the service layer
                when(this.petRecordService.getAllPetRecords()).thenReturn(petRecords);

                mvc.perform(get(URL))
                                .andExpect(status().isOk())
                                .andExpect(content().string(containsString("Fluffy")))
                                .andExpect(content().string(containsString("Whiskers")));
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" })
        void shouldDisplayNoPetRecordsAvailable() throws Exception {

                // Mock an empty list
                when(this.petRecordService.getAllPetRecords()).thenReturn(List.of());

                mvc.perform(get(URL))
                                .andExpect(status().isOk())
                                .andExpect(content().string(containsString("No records available.")));
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" })
        void shouldAddNewPetRecord() throws Exception {

                // Mock the save method for void methods
                doNothing().when(petRecordService).save(any(PetRecord.class));

                // Test a POST request to add the new record
                mvc.perform(post("/records/save")
                                .param("name", "Buddy")
                                .param("breed", "Labrador")
                                .param("dateOfBirth", "2020-10-20")
                                .param("veterinarian", "Dr. White"))
                                .andExpect(status().is3xxRedirection());
        }
}
