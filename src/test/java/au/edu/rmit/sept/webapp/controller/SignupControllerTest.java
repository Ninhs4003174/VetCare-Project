package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SignupControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PetService petService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private SignupController signupController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowSignupForm() {
        Model model = mock(Model.class);
        String viewName = signupController.showSignupForm(model);
        assertEquals("signup-client", viewName); // Ensure it returns the correct view
    }

    @Test
    void testRegisterUser_InvalidEmail() {
        // Arrange
        String invalidEmail = "invalidEmail";
        when(redirectAttributes.addFlashAttribute(anyString(), any())).thenReturn(redirectAttributes);

        // Act
        String result = signupController.registerUser(
                "testUser", invalidEmail, "Password1", "PetName", "Dog", 5, "A cute dog", redirectAttributes);

        // Assert
        assertEquals("redirect:/signup-client", result);
        verify(redirectAttributes).addFlashAttribute("message", "Invalid email format");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    void testRegisterUser_UsernameTooLong() {
        // Arrange
        String longUsername = "thisIsAVeryLongUsernameExceedingLimit";
        when(redirectAttributes.addFlashAttribute(anyString(), any())).thenReturn(redirectAttributes);

        // Act
        String result = signupController.registerUser(
                longUsername, "test@example.com", "Password1", "PetName", "Dog", 5, "A cute dog", redirectAttributes);

        // Assert
        assertEquals("redirect:/signup-client", result);
        verify(redirectAttributes).addFlashAttribute("message", "Username must not exceed 20 characters");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    void testRegisterUser_UsernameTaken() {
        // Arrange
        when(userService.isUsernameTaken("testUser")).thenReturn(true);
        when(redirectAttributes.addFlashAttribute(anyString(), any())).thenReturn(redirectAttributes);

        // Act
        String result = signupController.registerUser(
                "testUser", "test@example.com", "Password1", "PetName", "Dog", 5, "A cute dog", redirectAttributes);

        // Assert
        assertEquals("redirect:/signup-client", result);
        verify(redirectAttributes).addFlashAttribute("message", "Username is already taken");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    void testRegisterUser_EmailTaken() {
        // Arrange
        when(userService.isEmailTaken("test@example.com")).thenReturn(true);
        when(redirectAttributes.addFlashAttribute(anyString(), any())).thenReturn(redirectAttributes);

        // Act
        String result = signupController.registerUser(
                "testUser", "test@example.com", "Password1", "PetName", "Dog", 5, "A cute dog", redirectAttributes);

        // Assert
        assertEquals("redirect:/signup-client", result);
        verify(redirectAttributes).addFlashAttribute("message", "Email is already taken");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    void testRegisterUser_WeakPassword() {
        // Arrange
        when(redirectAttributes.addFlashAttribute(anyString(), any())).thenReturn(redirectAttributes);

        // Act
        String result = signupController.registerUser(
                "testUser", "test@example.com", "weakpass", "PetName", "Dog", 5, "A cute dog", redirectAttributes);

        // Assert
        assertEquals("redirect:/signup-client", result);
        verify(redirectAttributes).addFlashAttribute("message",
                "Password must be at least 8 characters long and contain at least one number");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    void testRegisterUser_InvalidPetAge() {
        // Arrange
        when(redirectAttributes.addFlashAttribute(anyString(), any())).thenReturn(redirectAttributes);

        // Act
        String result = signupController.registerUser(
                "testUser", "test@example.com", "Password1", "PetName", "Dog", 25, "A cute dog", redirectAttributes);

        // Assert
        assertEquals("redirect:/signup-client", result);
        verify(redirectAttributes).addFlashAttribute("message", "Pet age must be between 0 and 20 years");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    void testRegisterUser_SuccessfulRegistration() {
        // Arrange
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPets(new HashSet<>());
        when(userService.findUserByEmail("test@example.com")).thenReturn(newUser);
        when(redirectAttributes.addFlashAttribute(anyString(), any())).thenReturn(redirectAttributes);

        // Act
        String result = signupController.registerUser(
                "testUser", "test@example.com", "Password1", "PetName", "Dog", 5, "A cute dog", redirectAttributes);

        // Assert
        assertEquals("redirect:/login-client", result);
        verify(userService).registerUser("testUser", "test@example.com", "Password1", UserRole.CLIENT, null);
        verify(petService).addPet(any(Pet.class));
        verify(redirectAttributes).addFlashAttribute("message", "User and pet registered successfully!");
        verify(redirectAttributes).addFlashAttribute("success", true);
    }

    
}
