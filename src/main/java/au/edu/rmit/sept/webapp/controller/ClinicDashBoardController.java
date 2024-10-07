package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ClinicDashBoardController {

    @Autowired
    private UserService userService;

    @GetMapping("/clinichome")
    public String clinicHome() {
        return "clinic-dashboard/clinichome";
    }

    @GetMapping("/vets")
    public String vetList(Model model, Authentication authentication) {
        User clinic = userService.findByUsername(authentication.getName());
        List<User> vets = userService.getVetsByClinicId(clinic.getId());
        model.addAttribute("users", vets);
        return "clinic-dashboard/vets";
    }

    @GetMapping("/clinic-add-vet")
    public String addVetForm(Model model) {
        model.addAttribute("user", new User());
        return "clinic-dashboard/add-vet";
    }

    @PostMapping("/clinic-add-vet")
    public String addVet(@ModelAttribute User user, Authentication authentication) {
        User clinic = userService.findByUsername(authentication.getName());
        user.setRole(UserRole.VET);
        user.setClinicId(clinic.getId());
        userService.saveUser(user);
        return "redirect:/vets";
    }
}