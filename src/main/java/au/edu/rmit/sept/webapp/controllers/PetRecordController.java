package au.edu.rmit.sept.webapp.controllers;

import au.edu.rmit.sept.webapp.models.PetRecord;
import au.edu.rmit.sept.webapp.services.PetRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/records")
public class PetRecordController {

    @Autowired
    private PetRecordService petRecordService;

    // Serve the list of all pet records
    @GetMapping
    public String getAllRecords(Model model) {
        model.addAttribute("records", petRecordService.getAllPetRecords());
        return "records"; // This corresponds to records.html
    }

    // Serve the new record form page
    @GetMapping("/new")
    public String showNewRecordForm(Model model) {
        PetRecord petRecord = new PetRecord();
        model.addAttribute("petRecord", petRecord);
        return "new_record"; // This corresponds to new_record.html
    }

    // Handle form submission to save the new pet record
    @PostMapping("/save")
    public String saveRecord(@ModelAttribute("petRecord") PetRecord petRecord) {
        petRecordService.save(petRecord);
        return "redirect:/records"; // Redirect to records list after saving
    }

    // Serve the edit record form page
    @GetMapping("/edit/{id}")
    public String showEditRecordForm(@PathVariable Long id, Model model) {
        PetRecord petRecord = petRecordService.getPetRecordById(id);
        model.addAttribute("petRecord", petRecord);
        return "edit_record"; // This corresponds to edit_record.html
    }

    // Handle form submission to update an existing pet record
    @PostMapping("/update/{id}")
    public String updateRecord(@PathVariable Long id, @ModelAttribute("petRecord") PetRecord petRecord) {
        PetRecord existingRecord = petRecordService.getPetRecordById(id);
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
        return "redirect:/records"; // Redirect to records list after updating
    }

    // Delete a pet record
    @GetMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Long id) {
        petRecordService.delete(id);
        return "redirect:/records"; // Redirect to records list after deletion
    }
}
