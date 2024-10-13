package au.edu.rmit.sept.webapp.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

class LoginControllerTestsTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LoginController controller;

    private User clientUser;
    private User receptionistUser;
    private User vetUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clientUser = new User();
        clientUser.setRole(UserRole.CLIENT);

        receptionistUser = new User();
        receptionistUser.setRole(UserRole.RECEPTIONIST);

        vetUser = new User();
        vetUser.setRole(UserRole.VET);
    }

    @Test
    void testLoginPage() {
        String viewName = controller.login();
        assertEquals("login-client", viewName);
    }

    @Test
    void testHandleLogin_InvalidCredentials() {
        when(userService.authenticate("wrongUser", "wrongPass")).thenReturn(null);

        String viewName = controller.handleLogin("wrongUser", "wrongPass", model, redirectAttributes);

        assertEquals("login-client", viewName);
        verify(model).addAttribute("error", "Invalid username or password");
        verify(userService, times(1)).authenticate("wrongUser", "wrongPass");
    }

    @Test
    void testHandleLogin_ClientSuccess() {
        when(userService.authenticate("clientUser", "correctPass")).thenReturn(clientUser);

        String viewName = controller.handleLogin("clientUser", "correctPass", model, redirectAttributes);

        assertEquals("redirect:/userhome", viewName);
        verify(userService, times(1)).authenticate("clientUser", "correctPass");
    }

    @Test
    void testHandleLogin_ReceptionistSuccess() {
        when(userService.authenticate("receptionistUser", "correctPass")).thenReturn(receptionistUser);

        String viewName = controller.handleLogin("receptionistUser", "correctPass", model, redirectAttributes);

        assertEquals("redirect:/receptionisthome", viewName);
        verify(userService, times(1)).authenticate("receptionistUser", "correctPass");
    }

    @Test
    void testHandleLogin_VetSuccess() {
        when(userService.authenticate("vetUser", "correctPass")).thenReturn(vetUser);

        String viewName = controller.handleLogin("vetUser", "correctPass", model, redirectAttributes);

        assertEquals("redirect:/vethome", viewName);
        verify(userService, times(1)).authenticate("vetUser", "correctPass");
    }

    @Test
    void testUserHome() {
        ModelAndView modelAndView = controller.userHome();
        assertViewName(modelAndView, "userhome");
    }
}
