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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void showProfile_userExists_shouldReturnProfileView() {
        User mockUser = new User();
        mockUser.setUsername("testuser");

        when(userService.findByUsername("testuser")).thenReturn(mockUser);

        String viewName = profileController.showProfile(model);

        verify(model, times(1)).addAttribute("user", mockUser);
        assertEquals("profile", viewName);
    }

    @Test
    void showProfile_userNotFound_shouldReturnErrorView() {
        when(userService.findByUsername("testuser")).thenReturn(null);

        String viewName = profileController.showProfile(model);

        assertEquals("error", viewName);
    }
}
