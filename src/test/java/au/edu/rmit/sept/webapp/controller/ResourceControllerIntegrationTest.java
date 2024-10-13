package au.edu.rmit.sept.webapp.controller;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import au.edu.rmit.sept.webapp.model.Resource;
import au.edu.rmit.sept.webapp.service.ResourceService;

@SpringBootTest
@AutoConfigureMockMvc
class ResourceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceService resourceService;

    @Test
    @WithMockUser(username = "vetuser", roles = {"VET"})
    void testViewResources() throws Exception {
        when(resourceService.getApprovedResources()).thenReturn(Collections.emptyList());

        ResultActions result = mockMvc.perform(get("/resources")
                .with(user("vetuser").roles("VET")));

        result.andExpect(status().isOk())
              .andExpect(view().name("resources/list"))
              .andExpect(model().attributeExists("resources"));
    }

    @Test
    @WithMockUser(username = "adminuser", roles = {"ADMIN"})
    void testSaveResource() throws Exception {
        Resource resource = new Resource();
        resource.setTitle("Test Resource");

        ResultActions result = mockMvc.perform(post("/resources/save")
                .flashAttr("resource", resource)
                .with(user("adminuser").roles("ADMIN")));

        result.andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrl("/resources"));
    }

    @Test
    @WithMockUser(username = "adminuser", roles = {"ADMIN"})
    void testDeleteResource() throws Exception {
        ResultActions result = mockMvc.perform(get("/resources/delete/1")
                .with(user("adminuser").roles("ADMIN")));

        result.andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrl("/resources"));
    }
}
