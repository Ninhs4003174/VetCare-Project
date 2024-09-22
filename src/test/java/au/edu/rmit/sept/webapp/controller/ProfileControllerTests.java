package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileControllerTests {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setAddress("123 Test St");
        user.setPhoneNumber("1234567890");

        // Set up mock security context
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testShowProfile_UserFound() {
        when(userService.findByUsername("testUser")).thenReturn(user);

        String viewName = profileController.showProfile(model);

        assertEquals("profile", viewName);
        verify(model).addAttribute("user", user);
    }

    @Test
    public void testShowProfile_UserNotFound() {
        when(userService.findByUsername("testUser")).thenReturn(null);

        String viewName = profileController.showProfile(model);

        assertEquals("error", viewName);
        verify(model).addAttribute("message", "User not found");
    }

    @Test
    public void testShowEditUserForm_UserFound() {
        when(userService.findByUsername("testUser")).thenReturn(user);

        String viewName = profileController.showEditUserForm(model);

        assertEquals("edit-user", viewName);
        verify(model).addAttribute("user", user);
    }

    @Test
    public void testShowEditUserForm_UserNotFound() {
        when(userService.findByUsername("testUser")).thenReturn(null);

        String viewName = profileController.showEditUserForm(model);

        assertEquals("error", viewName);
        verify(model).addAttribute("message", "User not found");
    }

    @Test
    public void testEditUser_UserFound() {
        when(userService.findById(1L)).thenReturn(user);

        String result = profileController.editUser(1L, "456 New St", "0987654321", redirectAttributes);

        assertEquals("redirect:/profile", result);
        verify(userService).updateUser(user);
        assertEquals("456 New St", user.getAddress());
        assertEquals("0987654321", user.getPhoneNumber());
        verify(redirectAttributes).addFlashAttribute("message", "User details updated successfully!");
        verify(redirectAttributes).addFlashAttribute("success", true);
    }

    @Test
    public void testEditUser_UserNotFound() {
        when(userService.findById(1L)).thenReturn(null);

        String result = profileController.editUser(1L, "456 New St", "0987654321", redirectAttributes);

        assertEquals("redirect:/edit-user", result);
        verify(redirectAttributes).addFlashAttribute("message", "User not found");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    public void testEditUser_ExceptionHandling() {
        when(userService.findById(1L)).thenThrow(new RuntimeException("Database error"));

        String result = profileController.editUser(1L, "456 New St", "0987654321", redirectAttributes);

        assertEquals("redirect:/edit-user", result);
        verify(redirectAttributes).addFlashAttribute("message", "Failed to edit user: Database error");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }
}
