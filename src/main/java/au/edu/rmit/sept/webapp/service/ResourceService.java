package au.edu.rmit.sept.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.model.Resource;
import au.edu.rmit.sept.webapp.repository.ResourceRepository;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    // Get all resources with 'APPROVED' status
    public List<Resource> getApprovedResources() {
        return resourceRepository.findByStatus("APPROVED");
    }

    // Get resources by status (e.g., 'PENDING', 'APPROVED', 'REJECTED')
    public List<Resource> getResourcesByStatus(String status) {
        return resourceRepository.findByStatus(status);
    }

    // Get all resources (for admin view, including pending ones)
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    // Add a new resource with 'PENDING' status
    public void addResource(Resource resource) {
        resource.setStatus("PENDING");
        resourceRepository.save(resource);
    }

    // Find a resource by its ID
    public Resource getResourceById(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    // Update a resource
    public void updateResource(Resource resource) {
        resourceRepository.save(resource);
    }

    // Approve a resource by ID
    public void approveResource(Long id) {
        Resource resource = getResourceById(id);
        if (resource != null) {
            resource.setStatus("APPROVED");
            updateResource(resource);
        }
    }

    // Deny a resource by ID
    public void denyResource(Long id) {
        Resource resource = getResourceById(id);
        if (resource != null) {
            resource.setStatus("REJECTED");
            updateResource(resource);
        }
    }

    // Delete a resource by ID
    public void deleteResourceById(Long id) {
        resourceRepository.deleteById(id);
    }

    // Get resources with 'PENDING' status
    public List<Resource> getPendingResources() {
        return resourceRepository.findByStatus("PENDING");
    }
}
