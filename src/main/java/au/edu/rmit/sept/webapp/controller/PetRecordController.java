package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.PetRecord;
import au.edu.rmit.sept.webapp.model.Vet;
import au.edu.rmit.sept.webapp.service.PetRecordService;
import au.edu.rmit.sept.webapp.service.VetService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/records")
public class PetRecordController {

    @Autowired
    private PetRecordService petRecordService;

    @Autowired
    private VetService vetService;

    // Display all pet records
    @GetMapping
    public String getAllRecords(Model model) {
        model.addAttribute("records", petRecordService.getAllPetRecords());
        return "vet-dashboard/records"; // This corresponds to records.html
    }

    // Show form to create a new pet record
    @GetMapping("/new")
    public String showNewRecordForm(Model model) {
        PetRecord petRecord = new PetRecord();
        model.addAttribute("petRecord", petRecord);
        return "vet-dashboard/new_record"; // This corresponds to new_record.html
    }

    // Save a new pet record
    @PostMapping("/save")
    public String saveRecord(@ModelAttribute PetRecord petRecord, @RequestParam("vetId") Long vetId) {
        Vet vet = vetService.getVetById(vetId);
        if (vet != null) {
            petRecord.setVet(vet);
        }
        petRecordService.save(petRecord);
        return "redirect:/records"; // Redirect to records list after saving
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

    // Download PDF of a specific pet record
    @RequestMapping("/download-pdf/{id}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable Long id) throws IOException {
        PetRecord record = petRecordService.getPetRecordById(id);

        ByteArrayInputStream pdfStream = generatePdf(record);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=record_" + record.getId() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

    private ByteArrayInputStream generatePdf(PetRecord record) {
        // PDF generation logic (use iText or other PDF library)
        return new ByteArrayInputStream(new byte[0]); // Replace with actual PDF generation logic.
    }
}
