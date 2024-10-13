package au.edu.rmit.sept.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import au.edu.rmit.sept.webapp.model.Resource;
import au.edu.rmit.sept.webapp.service.ResourceService;

@Controller
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    // View all approved resources
    @GetMapping("/resources")
    public String viewResources(Model model) {
        List<Resource> resources = resourceService.getApprovedResources();
        model.addAttribute("resources", resources);
        return "resources/list";
    }

    // Show form to add a new resource
    @GetMapping("/resources/new")
    public String showNewResourceForm(Model model) {
        model.addAttribute("resource", new Resource());
        return "resources/new";
    }

    // Save a new resource with 'PENDING' status
    @PostMapping("/resources/save")
    public String saveResource(@ModelAttribute("resource") Resource resource, RedirectAttributes redirectAttributes) {
        resourceService.addResource(resource);  // Save the resource with pending status
        redirectAttributes.addFlashAttribute("message", "Your article is submitted, an admin will approve or deny your post.");
        return "redirect:/resources";
    }

    // Delete a resource by ID
    @GetMapping("/resources/delete/{id}")
    public String deleteResource(@PathVariable("id") Long id) {
        resourceService.deleteResourceById(id);
        return "redirect:/resources";
    }

    // View individual resource details
    @GetMapping("/resources/{id}")
    public String viewResource(@PathVariable Long id, Model model) {
        Resource resource = resourceService.getResourceById(id);
        model.addAttribute("resource", resource);
        return "resources/view";
    }

    // View pending resources for admin approval
    @GetMapping("/resources/approvals")
    public String viewPendingResources(Model model) {
        List<Resource> pendingResources = resourceService.getPendingResources();
        model.addAttribute("pendingResources", pendingResources);
        return "admin-dashboard/resource-approvals";
    }

    // Approve a resource by ID
    @PostMapping("/resources/approve/{id}")
    public String approveResource(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        resourceService.approveResource(id);
        redirectAttributes.addFlashAttribute("message", "Resource approved successfully.");
        return "redirect:/resources/approvals";
    }

    // Deny a resource by ID
    @PostMapping("/resources/deny/{id}")
    public String denyResource(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        resourceService.denyResource(id);
        redirectAttributes.addFlashAttribute("message", "Resource denied successfully.");
        return "redirect:/resources/approvals";
    }
}
