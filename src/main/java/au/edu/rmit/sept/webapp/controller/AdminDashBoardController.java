package au.edu.rmit.sept.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashBoardController {

    @GetMapping("/admin-dashboard/adminlist")
    public String adminList() {
        return "admin-dashboard/adminlist";
    }

    @GetMapping("/admin-dashboard/cliniclist")
    public String clinicList() {
        return "admin-dashboard/cliniclist";
    }

    @GetMapping("/admin-dashboard/userlist")
    public String userList() {
        return "admin-dashboard/userlist";
    }

    @GetMapping("/admin-dashboard/vetlist")
    public String vetList() {
        return "admin-dashboard/vetlist";
    }
}