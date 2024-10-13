package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.PetRecord;
import au.edu.rmit.sept.webapp.model.Vet;
import au.edu.rmit.sept.webapp.service.PetRecordService;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.VetService;
import au.edu.rmit.sept.webapp.model.User;

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
    public String showNewPetRecordForm(Model model) {
        model.addAttribute("petRecord", new PetRecord());

        // Fetching all users and vets from the database
        Iterable<User> users = userService.getAllUsers();
        List<Vet> vets = vetService.getAllVets();

        model.addAttribute("users", users);
        model.addAttribute("vets", vets);

        return "vet-dashboard/new_record"; // Thymeleaf template name
    }

    // Method to handle form submission
    @PostMapping("/save")
    public String saveNewPetRecord(@ModelAttribute PetRecord petRecord) {
        Vet selectedVet = petRecord.getVet();
        if (selectedVet != null) {
            Vet fetchedVet = vetService.getVetById(selectedVet.getVetId());
            petRecord.setVet(fetchedVet);
        }

        petRecordService.save(petRecord);
        return "redirect:/records";
    }

    // Show form to edit an existing pet record
    @GetMapping("/edit/{id}")
    public String showEditRecordForm(@PathVariable Long id, Model model) {
        PetRecord petRecord = petRecordService.getPetRecordById(id);
        List<Vet> vets = vetService.getAllVets(); // Fetch all vets for the dropdown
        model.addAttribute("petRecord", petRecord);
        model.addAttribute("vets", vets); // Add the list of vets to the model
        return "vet-dashboard/edit_record"; // This corresponds to edit_record.html
    }

    // Method to handle deleting a pet record by ID
    @PostMapping("/delete/{id}")
    public String deletePetRecord(@PathVariable("id") Long id) {
        petRecordService.delete(id); // Call the service to delete the record
        return "redirect:/records"; // Redirect to the records list after deleting
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
            existingRecord.setLastVisit(petRecord.getLastVisit());
            existingRecord.setAllergies(petRecord.getAllergies());
            existingRecord.setPrescriptions(petRecord.getPrescriptions());
            existingRecord.setVaccinationHistory(petRecord.getVaccinationHistory());
            existingRecord.setRecentTests(petRecord.getRecentTests());
            existingRecord.setRecentSurgeries(petRecord.getRecentSurgeries());
            existingRecord.setDietaryRecommendations(petRecord.getDietaryRecommendations());
            existingRecord.setNotes(petRecord.getNotes());

            // Fetch and assign the selected vet during update
            Vet selectedVet = vetService.getVetById(vetId);
            if (selectedVet != null) {
                existingRecord.setVet(selectedVet);
            }

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
                        + (record.getVet() != null ? record.getVet().getClinicName() : "No Vet Assigned"));
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
