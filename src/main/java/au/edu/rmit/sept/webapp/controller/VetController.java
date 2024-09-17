package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Vet;
import au.edu.rmit.sept.webapp.service.VetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class VetController {

    @Autowired
    private VetService vetService;

    @GetMapping("/profile")
    public String showProfilePage(Model model) {
        List<Vet> vets = vetService.getAllVets();
        model.addAttribute("vets", vets);
        return "profile"; // This should match your Thymeleaf template name
    }
}
