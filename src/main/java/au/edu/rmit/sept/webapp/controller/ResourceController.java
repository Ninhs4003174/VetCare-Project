package au.edu.rmit.sept.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import au.edu.rmit.sept.webapp.model.Resource;
import au.edu.rmit.sept.webapp.service.ResourceService;

@Controller
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/resources")
    public String viewResources(Model model) {
        List<Resource> resources = resourceService.getAllResources();
        model.addAttribute("resources", resources);
        return "resources/list";
    }

    @GetMapping("/resources/new")
    public String showNewResourceForm(Model model) {
        model.addAttribute("resource", new Resource());
        return "resources/new";
    }

    @PostMapping("/resources/save")
    public String saveResource(@ModelAttribute("resource") Resource resource) {
        resourceService.addResource(resource);
        return "redirect:/resources";
    }

    // Add delete mapping for removing a resource
    @GetMapping("/resources/delete/{id}")
    public String deleteResource(@PathVariable("id") Long id) {
        resourceService.deleteResourceById(id);  // Call delete method
        return "redirect:/resources";
    }
}
