package au.edu.rmit.sept.webapp.controllers;

import au.edu.rmit.sept.webapp.models.PetRecord;
import au.edu.rmit.sept.webapp.services.PetRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PetRecordController {

    @Autowired
    private PetRecordService petRecordService;

    @GetMapping("/records")
    public String getAllRecords(Model model) {
        model.addAttribute("records", petRecordService.getAllPetRecords());
        return "records";
    }

    @GetMapping("/records/{id}")
    public String getPetRecord(@PathVariable Long id, Model model) {
        PetRecord record = petRecordService.getPetRecordById(id);
        model.addAttribute("record", record);
        return "view_record"; // Make sure to have a `view_record.html` template
    }

    @GetMapping("/records/new")
    public String createNewRecord(Model model) {
        PetRecord petRecord = new PetRecord();
        model.addAttribute("record", petRecord);
        return "new_record"; // Corresponding form page to create a new record
    }

    @PostMapping("/records")
    public String savePetRecord(@ModelAttribute("record") PetRecord petRecord) {
        petRecordService.savePetRecord(petRecord);
        return "redirect:/records";
    }

    @GetMapping("/records/edit/{id}")
    public String editPetRecord(@PathVariable Long id, Model model) {
        PetRecord record = petRecordService.getPetRecordById(id);
        model.addAttribute("record", record);
        return "edit_record"; // Corresponding form page to edit the record
    }
}
