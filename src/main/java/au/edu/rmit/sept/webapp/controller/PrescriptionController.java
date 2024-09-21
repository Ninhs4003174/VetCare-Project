package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.Vet;
import au.edu.rmit.sept.webapp.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @GetMapping("/prescriptions-management")
    public String showPrescriptionsManagement(Model model) {
        // Fetch pending prescriptions and add them to the model
        model.addAttribute("pendingPrescriptions", prescriptionService.getPendingPrescriptions());
        return "prescriptions-management"; // Return the view name for the HTML page
    }

    @PostMapping("/submit-prescription")
    public String submitPrescription(@RequestParam Long petId,
            @RequestParam Long vetId,
            @RequestParam String medicationName,
            @RequestParam(required = false) String dosage,
            @RequestParam(required = false) String instructions) {
        Vet vet = prescriptionService.findVetById(vetId);
        if (vet != null) {
            Prescription prescription = new Prescription(petId, vet, medicationName, dosage, instructions);
            prescriptionService.requestPrescription(prescription);
        }
        return "redirect:/prescriptions-management"; // Redirect to view the pending requests
    }

    @GetMapping("/search-clinic")
    @ResponseBody
    public List<Vet> searchClinics(@RequestParam String query) {
        return prescriptionService.searchClinics(query);
    }
}
