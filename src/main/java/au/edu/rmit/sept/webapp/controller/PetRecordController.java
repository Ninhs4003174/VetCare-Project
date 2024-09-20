package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.PetRecord;
import au.edu.rmit.sept.webapp.service.PetRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/records")
public class PetRecordController {

    @Autowired
    private PetRecordService service;

    @GetMapping
    public String viewRecordsPage(Model model) {
        List<PetRecord> records = service.getAllRecords();
        model.addAttribute("records", records);
        return "records";
    }

    @GetMapping("/new")
    public String showNewRecordForm(Model model) {
        PetRecord petRecord = new PetRecord();
        model.addAttribute("petRecord", petRecord);
        return "new_record";
    }

    @PostMapping("/save")
    public String saveRecord(@ModelAttribute("petRecord") PetRecord petRecord) {
        service.saveRecord(petRecord);
        return "redirect:/records";
    }

    @GetMapping("/edit/{id}")
    public String showEditRecordForm(@PathVariable Long id, Model model) {
        PetRecord petRecord = service.getRecordById(id);
        model.addAttribute("petRecord", petRecord);
        return "edit_record";
    }

    @GetMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Long id) {
        service.deleteRecord(id);
        return "redirect:/records";
    }
}

//something wrong with how the controller is saving information
//watch CRUD Operations using Spring Boot + Spring MVC on YT
