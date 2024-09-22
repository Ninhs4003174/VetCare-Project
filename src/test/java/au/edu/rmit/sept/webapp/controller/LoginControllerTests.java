package au.edu.rmit.sept.webapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginControllerTests {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginPage() {
        String viewName = loginController.login();
        assertEquals("login", viewName);
    }

    @Test
    public void testHandleLogin_EmptyCredentials() {
        String result = loginController.handleLogin("", "", model);

        assertEquals("login", result);
        verify(model).addAttribute("error", "Username and password must be provided");
    }

    @Test
    public void testHandleLogin_InvalidCredentials() {
        String result = loginController.handleLogin("wrongUser", "wrongPass", model);

        assertEquals("login", result);
        verify(model).addAttribute("error", "Invalid username or password");
    }

    @Test
    public void testHandleLogin_Success() {
        String result = loginController.handleLogin("correctUser", "correctPass", model);

        assertEquals("redirect:/userhome", result);
    }

    @Test
    public void testUserHome() {
        String viewName = loginController.userhome();
        assertEquals("userhome", viewName);
    }

    @Test
    public void testHandleException() {
        Exception ex = new Exception("Test exception");
        String result = loginController.handleException(model, ex);

        assertEquals("error", result);
        verify(model).addAttribute("errorMessage", "An unexpected error occurred: Test exception");
    }
}
