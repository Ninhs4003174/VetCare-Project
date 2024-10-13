package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.PetRecord;
import au.edu.rmit.sept.webapp.model.Vet;
import au.edu.rmit.sept.webapp.service.PetRecordService;
import au.edu.rmit.sept.webapp.service.VetService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetRecordControllerUnitTest {

    @Mock
    private PetRecordService petRecordService;

    @Mock
    private VetService vetService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private PetRecordController petRecordController;

    private PetRecord petRecord;
    private Vet vet;

    @BeforeEach
    void setUp() {
        petRecord = new PetRecord();
        petRecord.setId(1L);
        petRecord.setName("Buddy");

        vet = new Vet();
        vet.setVetId(1L);
        vet.setClinicName("Vet Clinic");
    }

    @Test
    void testShowRecords() {
        List<PetRecord> petRecords = List.of(petRecord);
        when(petRecordService.getAllPetRecords()).thenReturn(petRecords);

        String view = petRecordController.showRecords(model);
        assertEquals("vet-dashboard/records", view);
        verify(model, times(1)).addAttribute("records", petRecords);
        verify(petRecordService, times(1)).getAllPetRecords();
    }

    @Test
    void testSaveNewPetRecord() {
        // Call the method under test
        String view = petRecordController.saveNewPetRecord(petRecord);

        // Assert the redirection
        assertEquals("redirect:/records", view);

        // Verify that petRecordService.save() was called exactly once
        verify(petRecordService, times(1)).save(any(PetRecord.class));
    }

    @Test
    void testShowNewPetRecordForm() {
        String view = petRecordController.showNewPetRecordForm(model);
        assertEquals("vet-dashboard/new_record", view);
        verify(model, times(1)).addAttribute(eq("petRecord"), any(PetRecord.class));
        verify(userService, times(1)).getAllUsers();
        verify(vetService, times(1)).getAllVets();
    }

    @Test
    void testUpdateRecord() {
        when(petRecordService.getPetRecordById(1L)).thenReturn(petRecord);
        when(vetService.getVetById(1L)).thenReturn(vet);

        String view = petRecordController.updateRecord(1L, petRecord, 1L);
        assertEquals("redirect:/records", view);
        verify(petRecordService, times(1)).update(any(PetRecord.class));
    }

    @Test
    void testDeleteRecord() {
        String view = petRecordController.deleteRecord(1L);
        assertEquals("redirect:/records", view);
        verify(petRecordService, times(1)).delete(1L);
    }

    @Test
    void testDownloadPdf() throws IOException {
        when(petRecordService.getPetRecordById(1L)).thenReturn(petRecord);

        ResponseEntity<InputStreamResource> response = petRecordController.downloadPdf(1L);

        assertEquals("attachment; filename=record_" + petRecord.getId() + ".pdf",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("application/pdf", response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));

        verify(petRecordService, times(1)).getPetRecordById(1L);
    }
}
