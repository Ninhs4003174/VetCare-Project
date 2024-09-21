package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SignupControllerTest {

    @InjectMocks
    private SignupController signupController;

    @Mock
    private UserService userService;

    @Mock
    private PetService petService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowSignupForm() {
        String viewName = signupController.showSignupForm(model);
        assertThat(viewName).isEqualTo("signup");
    }

    @Test
    void testRegisterUserWithInvalidEmail() {
        String viewName = signupController.registerUser("username", "invalid-email", "password123", "Buddy", "Dog", 5, "A friendly dog", model);
        verify(model).addAttribute("message", "Invalid email format");
        assertThat(viewName).isEqualTo("signup");
    }

    @Test
    void testRegisterUserWithUsernameTooLong() {
        String viewName = signupController.registerUser("verylongusername123", "user@example.com", "password123", "Buddy", "Dog", 5, "A friendly dog", model);
        verify(model).addAttribute("message", "Username must not exceed 20 characters");
        assertThat(viewName).isEqualTo("signup");
    }

    @Test
    void testRegisterUserWithTakenUsername() {
        when(userService.isUsernameTaken("takenUsername")).thenReturn(true);
        String viewName = signupController.registerUser("takenUsername", "user@example.com", "password123", "Buddy", "Dog", 5, "A friendly dog", model);
        verify(model).addAttribute("message", "Username is already taken");
        assertThat(viewName).isEqualTo("signup");
    }

    @Test
    void testRegisterUserWithTakenEmail() {
        when(userService.isUsernameTaken("username")).thenReturn(false);
        when(userService.isEmailTaken("taken@example.com")).thenReturn(true);
        String viewName = signupController.registerUser("username", "taken@example.com", "password123", "Buddy", "Dog", 5, "A friendly dog", model);
        verify(model).addAttribute("message", "Email is already taken");
        assertThat(viewName).isEqualTo("signup");
    }

    @Test
    void testRegisterUserWithWeakPassword() {
        String viewName = signupController.registerUser("username", "user@example.com", "weak", "Buddy", "Dog", 5, "A friendly dog", model);
        verify(model).addAttribute("message", "Password must be at least 8 characters long and contain at least one number");
        assertThat(viewName).isEqualTo("signup");
    }

    @Test
    void testRegisterUserWithInvalidPetAge() {
        String viewName = signupController.registerUser("username", "user@example.com", "password123", "Buddy", "Dog", 25, "A friendly dog", model);
        verify(model).addAttribute("message", "Pet age must be between 0 and 20 years");
        assertThat(viewName).isEqualTo("signup");
    }

    @Test
    void testRegisterUserSuccess() {
        when(userService.isUsernameTaken("username")).thenReturn(false);
        when(userService.isEmailTaken("user@example.com")).thenReturn(false);
        when(userService.findUserByEmail("user@example.com")).thenReturn(new User());

        String viewName = signupController.registerUser("username", "user@example.com", "password123", "Buddy", "Dog", 5, "A friendly dog", model);

        verify(model).addAttribute("message", "User and pet registered successfully!");
        assertThat(viewName).isEqualTo("redirect:/login");
    }

    @Test
    void testRegisterUserWithException() {
        when(userService.isUsernameTaken("username")).thenThrow(new RuntimeException("Database error"));
        String viewName = signupController.registerUser("username", "user@example.com", "password123", "Buddy", "Dog", 5, "A friendly dog", model);
        verify(model).addAttribute("message", "Registration failed: Database error");
        assertThat(viewName).isEqualTo("signup");
    }
}
