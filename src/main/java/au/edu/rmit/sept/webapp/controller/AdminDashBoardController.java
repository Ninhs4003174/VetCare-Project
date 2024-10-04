package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}