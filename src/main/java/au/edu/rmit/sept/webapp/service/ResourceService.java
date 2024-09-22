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

    // Get all resources
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    // Add a new resource
    public void addResource(Resource resource) {
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

    // Delete a resource by ID (Renaming this method to match the controller)
    public void deleteResourceById(Long id) {
        resourceRepository.deleteById(id);
    }
}
