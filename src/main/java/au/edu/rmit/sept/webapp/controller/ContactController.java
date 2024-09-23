package au.edu.rmit.sept.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import au.edu.rmit.sept.webapp.model.Contact;
import au.edu.rmit.sept.webapp.service.ContactService;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contact")
    public String showContactPage(Model model) {
        return "contact";
    }

    @PostMapping("/contact/submit")
    public String submitContactForm(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("message") String message,
            Model model) {

        Contact contact = new Contact(name, email, message);
        contactService.saveContact(contact);

        model.addAttribute("message", "Thank you for reaching out! We will get back to you soon.");
        return "contact";
    }

    @GetMapping("/contacts")
    public String showContactPages(Model model) {
        return "contact2";
    }

    @PostMapping("/contacts/submit")
    public String submitContactForms(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("message") String message,
            Model model) {

        Contact contact = new Contact(name, email, message);
        contactService.saveContact(contact);

        model.addAttribute("message", "Thank you for reaching out! We will get back to you soon.");
        return "contact2";
    }
}
