// package au.edu.rmit.sept.webapp.controller;

// import au.edu.rmit.sept.webapp.model.PetRecord;
// import au.edu.rmit.sept.webapp.model.Vet;
// import au.edu.rmit.sept.webapp.service.PetRecordService;
// import au.edu.rmit.sept.webapp.service.VetService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.ui.Model;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;
// import static org.junit.jupiter.api.Assertions.*;

// @ExtendWith(MockitoExtension.class)
// class PetRecordControllerUnitTest {

// @Mock
// private PetRecordService petRecordService;

// @Mock
// private VetService vetService;

// @Mock
// private Model model;

// @Mock
// private RedirectAttributes redirectAttributes;

// @InjectMocks
// private PetRecordController petRecordController;

// private PetRecord petRecord;
// private Vet vet;

// @BeforeEach
// void setUp() {
// petRecord = new PetRecord();
// petRecord.setId(1L);
// petRecord.setName("Buddy");

// vet = new Vet();
// vet.setVetId(1L);
// vet.setClinicName("Vet Clinic");
// }

// @Test
// void testGetAllRecords() {
// String view = petRecordController.getAllRecords(model);
// assertEquals("vet-dashboard/records", view);
// verify(petRecordService, times(1)).getAllPetRecords();
// }

// @Test
// void testsaveNewPetRecord() {
// // Mock the behavior of vetService
// when(vetService.getVetById(1L)).thenReturn(vet);

// // Call the saveNewPetRecord method with RedirectAttributes
// String view = petRecordController.saveNewPetRecord(petRecord, 1L,
// redirectAttributes);

// // Assert the view name after redirect
// assertEquals("redirect:/records", view);

// // Verify that petRecordService.save() was called exactly once
// verify(petRecordService, times(1)).save(any(PetRecord.class));
// }

// @Test
// void testShowNewRecordForm() {
// String view = petRecordController.showNewRecordForm(model);
// assertEquals("vet-dashboard/new_record", view);
// verify(model, times(1)).addAttribute(eq("petRecord"), any(PetRecord.class));
// }

// @Test
// void testUpdateRecord() {
// when(petRecordService.getPetRecordById(1L)).thenReturn(petRecord);
// String view = petRecordController.updateRecord(1L, petRecord, 1L);
// assertEquals("redirect:/records", view);
// verify(petRecordService, times(1)).update(petRecord);
// }
// }
