package au.edu.rmit.sept.webapp.controller;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import au.edu.rmit.sept.webapp.model.Resource;
import au.edu.rmit.sept.webapp.service.ResourceService;

class ResourceControllerTest {

    @Mock
    private ResourceService resourceService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ResourceController resourceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testViewResources() {
        List<Resource> resources = Collections.emptyList();
        when(resourceService.getApprovedResources()).thenReturn(resources);

        String viewName = resourceController.viewResources(model);
        assertEquals("resources/list", viewName);
        verify(model).addAttribute("resources", resources);
    }

    @Test
    void testShowNewResourceForm() {
        String viewName = resourceController.showNewResourceForm(model);
        assertEquals("resources/new", viewName);
        verify(model).addAttribute(eq("resource"), any(Resource.class));
    }

    @Test
    void testSaveResource_Success() {
        Resource resource = new Resource();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = resourceController.saveResource(resource, redirectAttributes);
        assertEquals("redirect:/resources", viewName);
        verify(resourceService).addResource(resource);
        verify(redirectAttributes).addFlashAttribute("message", "Your article is submitted, an admin will approve or deny your post.");
    }

    @Test
    void testDeleteResource() {
        Long resourceId = 1L;

        String viewName = resourceController.deleteResource(resourceId);
        assertEquals("redirect:/resources", viewName);
        verify(resourceService).deleteResourceById(resourceId);
    }
}
