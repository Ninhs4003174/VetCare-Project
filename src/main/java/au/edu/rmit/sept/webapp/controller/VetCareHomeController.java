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
public class VetCareHomeController {

    @Autowired
    private PetRecordService petRecordService;

    @GetMapping("/vetcaresystemhome")
    public String showHome() {
        return "vetcaresystemhome"; // Serve the role selection view
    }

    @PostMapping("/vetcaresystemhome/selectrole")
    public String handleRoleSelection(@RequestParam("role") String role) {
        System.out.println("Selected role: " + role); // Add this line for debugging
        // Redirect to the appropriate login page based on the role
        switch (role) {
            case "CLIENT":
                return "redirect:/login-client";
            case "RECEPTIONIST":
                return "redirect:/login-receptionist";
            case "VETERINARIAN":
                return "redirect:/login-veterinarian";
            default:
                // Handle unknown roles by redirecting back to the role selection page
                return "redirect:/vetcaresystemhome";
        }
    }

    @GetMapping("/records")
    public String getAllRecords(Model model) {
        model.addAttribute("records", petRecordService.getAllPetRecords());
        return "admin-dashboard/records";
    }

    @GetMapping("/records/new")
    public String showNewRecordForm(Model model) {
        PetRecord petRecord = new PetRecord();
        model.addAttribute("petRecord", petRecord);
        return "admin-dashboard/new_record";
    }

    @PostMapping("/records/save")
    public String saveRecord(@ModelAttribute PetRecord petRecord) {
        petRecordService.save(petRecord);
        return "redirect:/records";
    }

    @GetMapping("/records/edit/{id}")
    public String showEditRecordForm(@PathVariable Long id, Model model) {
        PetRecord petRecord = petRecordService.getPetRecordById(id);
        model.addAttribute("petRecord", petRecord);
        return "admin-dashboard/edit_record";
    }

    @PostMapping("/records/update/{id}")
    public String updateRecord(@PathVariable Long id, @ModelAttribute PetRecord petRecord) {
        PetRecord existingRecord = petRecordService.getPetRecordById(id);
        if (existingRecord != null) {
            // Update fields
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
        return "redirect:/records";
    }

    @GetMapping("/records/view/{id}")
    public String viewPetRecord(@PathVariable Long id, Model model) {
        PetRecord petRecord = petRecordService.getPetRecordById(id);
        model.addAttribute("petRecord", petRecord);
        return "admin-dashboard/view_record";
    }
}