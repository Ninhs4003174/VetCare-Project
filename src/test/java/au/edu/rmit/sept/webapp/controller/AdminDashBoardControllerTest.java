package au.edu.rmit.sept.webapp.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import au.edu.rmit.sept.webapp.controller.AdminDashBoardController;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.ResourceService;
import au.edu.rmit.sept.webapp.service.UserService;

@WebMvcTest(AdminDashBoardController.class)
public class AdminDashBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ResourceService resourceService;

    private List<User> adminList;
    private List<User> vetList;
    private List<User> clinicList;
    private List<User> userList;

    @BeforeEach
    public void setUp() {
        adminList = new ArrayList<>();
        vetList = new ArrayList<>();
        clinicList = new ArrayList<>();
        userList = new ArrayList<>();
        
        User admin = new User();
        admin.setRole(UserRole.ADMIN);
        adminList.add(admin);
        
        User vet = new User();
        vet.setRole(UserRole.VET);
        vetList.add(vet);
        
        User clinic = new User();
        clinic.setRole(UserRole.RECEPTIONIST);
        clinicList.add(clinic);
        
        User user = new User();
        user.setRole(UserRole.CLIENT);
        userList.add(user);
    }

    // Mocking an authenticated admin user for these tests
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAdminList() throws Exception {
        when(userService.getUsersByRole(UserRole.ADMIN)).thenReturn(adminList);

        mockMvc.perform(get("/adminlist"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-dashboard/adminlist"))
            .andExpect(model().attributeExists("users"));
        
        verify(userService, times(1)).getUsersByRole(UserRole.ADMIN);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testVetList() throws Exception {
        when(userService.getUsersByRole(UserRole.VET)).thenReturn(vetList);

        mockMvc.perform(get("/vetlist"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-dashboard/vetlist"))
            .andExpect(model().attributeExists("users"));
        
        verify(userService, times(1)).getUsersByRole(UserRole.VET);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testClinicList() throws Exception {
        when(userService.getUsersByRole(UserRole.RECEPTIONIST)).thenReturn(clinicList);

        mockMvc.perform(get("/cliniclist"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-dashboard/cliniclist"))
            .andExpect(model().attributeExists("users"));
        
        verify(userService, times(1)).getUsersByRole(UserRole.RECEPTIONIST);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUserList() throws Exception {
        when(userService.getUsersByRole(UserRole.CLIENT)).thenReturn(userList);

        mockMvc.perform(get("/userlist"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-dashboard/userlist"))
            .andExpect(model().attributeExists("users"));
        
        verify(userService, times(1)).getUsersByRole(UserRole.CLIENT);
    }

    // @Test
    // @WithMockUser(username = "admin", roles = {"ADMIN"})
    // public void testAddVetForm() throws Exception {
    //     when(userService.getUsersByRole(UserRole.RECEPTIONIST)).thenReturn(clinicList);

    //     mockMvc.perform(get("/vetlist"))
    //         .andExpect(status().isOk())
    //         .andExpect(view().name("admin-dashboard/vetlist"))
    //         .andExpect(model().attributeExists("users"));

        
    //     verify(userService, times(1)).getUsersByRole(UserRole.RECEPTIONIST);
    // }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAddUserForm() throws Exception {
        mockMvc.perform(get("/userlist"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-dashboard/userlist"))
            .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAddClinicForm() throws Exception {
        mockMvc.perform(get("/cliniclist"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-dashboard/cliniclist"))
            .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testResourceApprovals() throws Exception {
        mockMvc.perform(get("/resource-approvals"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-dashboard/resource-approvals"))
            .andExpect(model().attributeExists("resources"));
    }
}
