package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.PetRecord;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.Vet;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.PetRecordService;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.VetService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/records")
public class PetRecordController {

    @Autowired
    private PetRecordService petRecordService;

    @Autowired
    private VetService vetService;

    @Autowired
    private UserService userService;

    // Display all pet records
    @GetMapping
    public String getAllRecords(Model model) {
        model.addAttribute("records", petRecordService.getAllPetRecords());
        return "vet-dashboard/records"; // This corresponds to records.html
    }

    // Show form to create a new pet record
    @GetMapping("/new")
    public String showNewRecordForm(Model model) {
        // Create a new empty PetRecord object to bind the form fields
        PetRecord petRecord = new PetRecord();
        model.addAttribute("petRecord", petRecord);

        // Add necessary data like users and vets for dropdowns
        List<User> vets = userService.getUsersByRole(UserRole.VET); // Fetch vets from UserService
        List<User> users = userService.getUsersByRole(UserRole.CLIENT); // Assuming clients are the pet owners

        model.addAttribute("vets", vets); // List of vets for dropdown
        model.addAttribute("users", users); // List of pet owners for dropdown

        // Return the view for the form
        return "vet-dashboard/new_record";
    }

    // Save a new pet record
    @PostMapping("/records/save")
    public String saveNewPetRecord(PetRecord petRecord, @RequestParam("vetId") Long vetId,
            RedirectAttributes redirectAttributes) {
        Vet vet = vetService.getVetById(vetId);
        petRecord.setVet(vet); // Assign vet to petRecord
        petRecordService.save(petRecord); // Save petRecord

        redirectAttributes.addFlashAttribute("message", "Pet record saved successfully");
        return "redirect:/records";
    }

    // Show form to edit an existing pet record
    @GetMapping("/edit/{id}")
    public String showEditRecordForm(@PathVariable Long id, Model model) {
        PetRecord petRecord = petRecordService.getPetRecordById(id);
        model.addAttribute("petRecord", petRecord);
        return "vet-dashboard/edit_record"; // This corresponds to edit_record.html
    }

    // Update an existing pet record
    @PostMapping("/update/{id}")
    public String updateRecord(@PathVariable Long id, @ModelAttribute PetRecord petRecord,
            @RequestParam("vetId") Long vetId) {
        PetRecord existingRecord = petRecordService.getPetRecordById(id);

        if (existingRecord != null) {
            existingRecord.setName(petRecord.getName());
            existingRecord.setBreed(petRecord.getBreed());
            existingRecord.setDateOfBirth(petRecord.getDateOfBirth());
            existingRecord.setVeterinarian(petRecord.getVeterinarian());
            existingRecord.setLastVisit(petRecord.getLastVisit());
            existingRecord.setAllergies(petRecord.getAllergies());
            existingRecord.setPrescriptions(petRecord.getPrescriptions());
            existingRecord.setVaccinationHistory(petRecord.getVaccinationHistory());
            existingRecord.setRecentTests(petRecord.getRecentTests());
            existingRecord.setRecentSurgeries(petRecord.getRecentSurgeries());
            existingRecord.setDietaryRecommendations(petRecord.getDietaryRecommendations());
            existingRecord.setNotes(petRecord.getNotes());

            petRecordService.update(existingRecord);
        }

        return "redirect:/records"; // Redirect to records page after update
    }

    // View a specific pet record
    @GetMapping("/view/{id}")
    public String viewPetRecord(@PathVariable Long id, Model model) {
        PetRecord petRecord = petRecordService.getPetRecordById(id);
        model.addAttribute("petRecord", petRecord);
        return "vet-dashboard/view_record";
    }

    @GetMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Long id) {
        petRecordService.delete(id);
        return "redirect:/records"; // Redirect to the records list after deletion
    }

    // Download PDF of a specific pet record
    @RequestMapping("/download-pdf/{id}")
    public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable Long id) throws IOException {
        PetRecord record = petRecordService.getPetRecordById(id);

        ByteArrayInputStream pdfStream = generatePdf(record);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=record_" + record.getId() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

    private ByteArrayInputStream generatePdf(PetRecord record) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Create a new PDF document
        try (PDDocument document = new PDDocument()) {
            // Add a blank page
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a content stream to add content to the page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 750); // Set starting position on the page

                // Add title
                contentStream.showText("Pet Record for " + record.getName());
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA, 12); // Set font for the content

                // Add Pet details
                contentStream.showText("Breed: " + record.getBreed());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Date of Birth: " + record.getDateOfBirth());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Veterinarian: "
                        + (record.getVeterinarian() != null ? record.getVeterinarian() : "No Vet Assigned"));
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Last Visit: "
                        + (record.getLastVisit() != null ? record.getLastVisit().toString() : "No Visit Data"));

                // Add spacing before additional details
                contentStream.newLineAtOffset(0, -20);

                // Add Additional details
                contentStream
                        .showText("Allergies: " + (record.getAllergies() != null ? record.getAllergies() : "None"));
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Recent Surgeries: "
                        + (record.getRecentSurgeries() != null ? record.getRecentSurgeries() : "None"));
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Vaccination History: "
                        + (record.getVaccinationHistory() != null ? record.getVaccinationHistory() : "None"));
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(
                        "Recent Tests: " + (record.getRecentTests() != null ? record.getRecentTests() : "None"));
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(
                        "Prescriptions: " + (record.getPrescriptions() != null ? record.getPrescriptions() : "None"));
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Dietary Recommendations: "
                        + (record.getDietaryRecommendations() != null ? record.getDietaryRecommendations() : "None"));
                contentStream.newLineAtOffset(0, -15);
                contentStream
                        .showText("Notes: " + (record.getNotes() != null ? record.getNotes() : "No additional notes"));

                contentStream.endText();
            }

            // Save the document to the output stream
            document.save(out);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
