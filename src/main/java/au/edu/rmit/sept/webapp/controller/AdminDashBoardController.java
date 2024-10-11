package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminDashBoardController {

    @Autowired
    private UserService userService;

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

    @GetMapping("/delete-clinic/{id}")
    public String deleteClinic(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin-dashboard/cliniclist";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin-dashboard/userlist";
    }

    @GetMapping("/delete-vet/{id}")
    public String deleteVet(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin-dashboard/vetlist";
    }
}
