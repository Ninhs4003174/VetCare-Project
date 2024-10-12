package au.edu.rmit.sept.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import au.edu.rmit.sept.webapp.service.PetRecordService;
import au.edu.rmit.sept.webapp.service.VetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import au.edu.rmit.sept.webapp.model.PetRecord;
import au.edu.rmit.sept.webapp.model.Vet;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PetRecordController {

    @Autowired
    private PetRecordService petRecordService;

    @Autowired
    private VetService vetService; // Service to fetch Vet details

    // Display all pet records
    @GetMapping("/records")
    public String getAllRecords(Model model) {
        model.addAttribute("records", petRecordService.getAllPetRecords());
        return "vet-dashboard/records";
    }

    // Show form to create a new pet record
    @GetMapping("/records/new")
    public String showNewRecordForm(Model model) {
        PetRecord petRecord = new PetRecord();
        model.addAttribute("petRecord", petRecord);
        model.addAttribute("vets", vetService.getAllVets()); // Add list of vets for selection
        return "vet-dashboard/new_record";
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
        return "redirect:/vet/records";
    }

    // Show form to edit an existing pet record
    @GetMapping("/records/edit/{id}")
    public String showEditRecordForm(@PathVariable Long id, Model model) {
        PetRecord petRecord = petRecordService.getPetRecordById(id);
        model.addAttribute("petRecord", petRecord);
        model.addAttribute("vets", vetService.getAllVets()); // Add list of vets for selection
        return "vet-dashboard/edit_record";
    }

    // Update an existing pet record
    @PostMapping("/records/update/{id}")
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

            // Update Vet association
            Vet vet = vetService.getVetById(vetId);
            if (vet != null) {
                existingRecord.setVet(vet);
            }

            petRecordService.update(existingRecord);
        }
        return "redirect:/vet/records";
    }

    // View a specific pet record
    @GetMapping("/records/view/{id}")
    public String viewPetRecord(@PathVariable Long id, Model model) {
        PetRecord petRecord = petRecordService.getPetRecordById(id);
        model.addAttribute("petRecord", petRecord);
        return "vet-dashboard/view_record";
    }
}