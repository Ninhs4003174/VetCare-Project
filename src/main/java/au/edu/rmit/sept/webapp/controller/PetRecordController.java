package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.PetRecord;
import au.edu.rmit.sept.webapp.model.Vet;
import au.edu.rmit.sept.webapp.service.PetRecordService;
import au.edu.rmit.sept.webapp.service.VetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/records")
public class PetRecordController {

    @Autowired
    private PetRecordService petRecordService;

    @Autowired
    private VetService vetService; // Service to fetch Vet details

    // Display all pet records
    @GetMapping("/records")
    public String getAllRecords(Model model) {
        model.addAttribute("records", petRecordService.getAllPetRecords());
        return "records"; // This corresponds to records.html
    }

    // Show form to create a new pet record
    @GetMapping("/records/new")
    public String showNewRecordForm(Model model) {
        PetRecord petRecord = new PetRecord();
        model.addAttribute("petRecord", petRecord);
        return "new_record"; // This corresponds to new_record.html
    }

    // Save a new pet record
    @PostMapping("/records/save")
    public String saveRecord(@ModelAttribute PetRecord petRecord, @RequestParam("vetId") Long vetId) {
        // Set the associated Vet entity
        Vet vet = vetService.getVetById(vetId);
        if (vet != null) {
            petRecord.setVet(vet);
        }
        petRecordService.save(petRecord);
        return "redirect:/records"; // Redirect to records list after saving
    }

    // Show form to edit an existing pet record
    @GetMapping("/records/edit/{id}")
    public String showEditRecordForm(@PathVariable Long id, Model model) {
        PetRecord petRecord = petRecordService.getPetRecordById(id);
        model.addAttribute("petRecord", petRecord);
        return "edit_record"; // This corresponds to edit_record.html
    }

    // Update an existing pet record
    @PostMapping("/records/update/{id}")
    public String updateRecord(@PathVariable Long id, @ModelAttribute PetRecord petRecord,
            @RequestParam("vetId") Long vetId) {
        PetRecord existingRecord = petRecordService.getPetRecordById(id);

        if (existingRecord != null) {
            // Update the fields of the existing record with the form data
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

            petRecordService.update(existingRecord); // Call service to update the record
        }

        return "redirect:/records"; // Redirect to records page after update
    }

    // View a specific pet record
    @GetMapping("/records/view/{id}")
    public String viewPetRecord(@PathVariable Long id, Model model) {
        PetRecord petRecord = petRecordService.getPetRecordById(id);
        model.addAttribute("petRecord", petRecord);
        return "vet-dashboard/view_record";
    }
}
