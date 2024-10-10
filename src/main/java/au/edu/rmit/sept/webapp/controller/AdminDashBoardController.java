package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.PetRecord;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.PetRecordService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminDashBoardController {

    @Autowired
    private UserService userService;

    @Autowired
    private PetRecordService petRecordService;

    // User and Role Management
    @GetMapping("/adminlist")
    public String adminList(Model model) {
        List<User> admins = userService.getUsersByRole(UserRole.ADMIN);
        model.addAttribute("users", admins);
        return "admin-dashboard/adminlist";
    }

    @GetMapping("/cliniclist")
    public String clinicList(Model model) {
        List<User> clinics = userService.getUsersByRole(UserRole.RECEPTIONIST);
        model.addAttribute("users", clinics);
        return "admin-dashboard/cliniclist";
    }

    @GetMapping("/userlist")
    public String userList(Model model) {
        List<User> users = userService.getUsersByRole(UserRole.CLIENT);
        model.addAttribute("users", users);
        return "admin-dashboard/userlist";
    }

    @GetMapping("/vetlist")
    public String vetList(Model model) {
        List<User> vets = userService.getUsersByRole(UserRole.VET);
        model.addAttribute("users", vets);
        return "admin-dashboard/vetlist";
    }

    // Add users, clinics, and vets
    @GetMapping("/add-vet")
    public String addVetForm(Model model) {
        List<User> clinics = userService.getUsersByRole(UserRole.RECEPTIONIST);
        if (clinics.isEmpty()) {
            model.addAttribute("error", "No clinics available. Please add a clinic first.");
            return "admin-dashboard/vetlist";
        }
        model.addAttribute("user", new User());
        model.addAttribute("clinics", clinics);
        return "admin-dashboard/add-vet";
    }

    @PostMapping("/add-vet")
    public String addVet(@ModelAttribute User user) {
        user.setRole(UserRole.VET);
        userService.saveUser(user);
        return "redirect:/vetlist";
    }

    @GetMapping("/add-user")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin-dashboard/add-user";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute User user) {
        user.setRole(UserRole.CLIENT);
        userService.saveUser(user);
        return "redirect:/userlist";
    }

    @GetMapping("/add-clinic")
    public String addClinicForm(Model model) {
        model.addAttribute("user", new User());
        return "admin-dashboard/add-clinic";
    }

    @PostMapping("/add-clinic")
    public String addClinic(@ModelAttribute User user) {
        user.setRole(UserRole.RECEPTIONIST);
        userService.saveUser(user);
        return "redirect:/cliniclist";
    }

    @GetMapping("/add-admin")
    public String addAdminForm(Model model) {
        model.addAttribute("user", new User());
        return "admin-dashboard/add-admin";
    }

    @PostMapping("/add-admin")
    public String addAdmin(@ModelAttribute User user) {
        user.setRole(UserRole.ADMIN);
        userService.saveUser(user);
        return "redirect:/adminlist";
    }

    // Pet Records Management
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/records/create")
    public String createPetRecord(@ModelAttribute PetRecord petRecord) {
        petRecordService.save(petRecord);
        return "redirect:/records";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/records/delete/{id}")
    public String deletePetRecord(@PathVariable Long id) {
        petRecordService.delete(id);
        return "redirect:/records";
    }
}
