package au.edu.rmit.sept.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import au.edu.rmit.sept.webapp.service.PetRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import au.edu.rmit.sept.webapp.model.PetRecord;

@Controller
public class VetHomeController {

    @Autowired
    private PetRecordService petRecordService;

    @GetMapping("/vetcaresystemhome")
    public String showHome() {
        return "vetcaresystemhome";
    }

    @PostMapping("/vetcaresystemhome/selectrole")
    public String handleRoleSelection(@RequestParam("role") String role) {
        switch (role) {
            case "CLIENT":
                return "redirect:/login-client";
            case "RECEPTIONIST":
                return "redirect:/login-receptionist";
            case "VETERINARIAN":
                return "redirect:/login-veterinarian";
            default:
                return "redirect:/vetcaresystemhome";
        }
    }

    // Pet Records Management
    @GetMapping("/vet/records")
    public String getAllRecords(Model model) {
        model.addAttribute("records", petRecordService.getAllPetRecords());
        return "vet-dashboard/records";
    }

    @GetMapping("/vet/records/new")
    public String showNewRecordForm(Model model) {
        PetRecord petRecord = new PetRecord();
        model.addAttribute("petRecord", petRecord);
        return "vet-dashboard/new_record";
    }

    @PostMapping("/vet/records/save")
    public String saveRecord(@ModelAttribute PetRecord petRecord) {
        petRecordService.save(petRecord);
        return "redirect:/vet/records";
    }

    @GetMapping("/vet/records/edit/{id}")
    public String showEditRecordForm(@PathVariable Long id, Model model) {
        PetRecord petRecord = petRecordService.getPetRecordById(id);
        model.addAttribute("petRecord", petRecord);
        return "vet-dashboard/edit_record";
    }

    @PostMapping("/vet/records/update/{id}")
    public String updateRecord(@PathVariable Long id, @ModelAttribute PetRecord petRecord) {
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
        return "redirect:/vet/records";
    }

    @GetMapping("/vet/records/view/{id}")
    public String viewPetRecord(@PathVariable Long id, Model model) {
        PetRecord petRecord = petRecordService.getPetRecordById(id);
        model.addAttribute("petRecord", petRecord);
        return "vet-dashboard/view_record";
    }
}
