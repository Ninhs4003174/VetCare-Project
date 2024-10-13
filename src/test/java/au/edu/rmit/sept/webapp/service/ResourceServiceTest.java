package au.edu.rmit.sept.webapp.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import au.edu.rmit.sept.webapp.model.Resource;
import au.edu.rmit.sept.webapp.repository.ResourceRepository;

class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceService resourceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetApprovedResources() {
        List<Resource> resources = Collections.emptyList();
        when(resourceRepository.findByStatus("APPROVED")).thenReturn(resources);

        List<Resource> result = resourceService.getApprovedResources();
        assertEquals(resources, result);
        verify(resourceRepository).findByStatus("APPROVED");
    }

    @Test
    void testAddResource() {
        Resource resource = new Resource();
        resource.setTitle("Test Resource");

        resourceService.addResource(resource);
        verify(resourceRepository).save(resource);
    }

    @Test
    void testDeleteResourceById() {
        Long resourceId = 1L;

        resourceService.deleteResourceById(resourceId);
        verify(resourceRepository).deleteById(resourceId);
    }

    @Test
    void testGetResourceById() {
        Long resourceId = 1L;
        Resource resource = new Resource();
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));

        Resource result = resourceService.getResourceById(resourceId);
        assertEquals(resource, result);
    }
}
