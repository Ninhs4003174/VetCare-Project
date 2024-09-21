package au.edu.rmit.sept.webapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginPage() {
        String viewName = loginController.login();
        assertThat(viewName).isEqualTo("login");
    }

    @Test
    void testHandleLoginWithEmptyCredentials() {
        String viewName = loginController.handleLogin("", "", model);
        verify(model).addAttribute("error", "Username and password must be provided");
        assertThat(viewName).isEqualTo("login");
    }

    @Test
    void testHandleLoginWithInvalidCredentials() {
        String viewName = loginController.handleLogin("wrongUser", "wrongPass", model);
        verify(model).addAttribute("error", "Invalid username or password");
        assertThat(viewName).isEqualTo("login");
    }

    @Test
    void testHandleLoginWithValidCredentials() {
        String viewName = loginController.handleLogin("correctUser", "correctPass", model);
        assertThat(viewName).isEqualTo("redirect:/userhome");
    }

    @Test
    void testWelcomePage() {
        String viewName = loginController.welcome();
        assertThat(viewName).isEqualTo("welcome");
    }

    @Test
    void testUserHomePage() {
        String viewName = loginController.userhome();
        assertThat(viewName).isEqualTo("userhome");
    }

    @Test
    void testHandleException() {
        String viewName = loginController.handleException(model, new Exception("Test Exception"));
        verify(model).addAttribute("errorMessage", "An unexpected error occurred: Test Exception");
        assertThat(viewName).isEqualTo("error");
    }
}
