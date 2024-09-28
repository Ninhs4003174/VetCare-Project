package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.service.UserService;
import au.edu.rmit.sept.webapp.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SignupControllerTest {

    @InjectMocks
    private SignupController signupController;

    @Mock
    private UserService userService;

    @Mock
    private PetService petService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowSignupForm() {
        String viewName = signupController.showSignupForm(model);
        assertEquals("signup", viewName);
    }

    @Test
    public void testRegisterUser_Success() {
        String username = "testuser";
        String email = "test@example.com";
        String password = "Password1";
        String petName = "Fluffy";
        String petType = "Dog";
        int petAge = 5;
        String petBio = "Loves to play";
        UserRole role = UserRole.CLIENT;

        when(userService.isUsernameTaken(username)).thenReturn(false);
        when(userService.isEmailTaken(email)).thenReturn(false);
        when(userService.findUserByEmail(email)).thenReturn(new User());

        String result = signupController.registerUser(username, email, password, petName, petType, petAge, petBio, role,
                redirectAttributes);

        assertEquals("redirect:/login", result);
        verify(redirectAttributes).addFlashAttribute("message", "User and pet registered successfully!");
        verify(redirectAttributes).addFlashAttribute("success", true);
        verify(petService).addPet(any(Pet.class));
    }

    @Test
    public void testRegisterUser_InvalidEmail() {
        String result = signupController.registerUser("testuser", "invalid-email", "Password1", "Fluffy", "Dog", 5,
                "Loves to play", UserRole.CLIENT, redirectAttributes);

        assertEquals("redirect:/signup", result);
        verify(redirectAttributes).addFlashAttribute("message", "Invalid email format");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    public void testRegisterUser_UsernameTaken() {
        String username = "testuser";
        String email = "test@example.com";
        String password = "Password1";

        when(userService.isUsernameTaken(username)).thenReturn(true);

        String result = signupController.registerUser(username, email, password, "Fluffy", "Dog", 5, "Loves to play",
                UserRole.CLIENT,redirectAttributes);

        assertEquals("redirect:/signup", result);
        verify(redirectAttributes).addFlashAttribute("message", "Username is already taken");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    public void testRegisterUser_EmailTaken() {
        String username = "testuser";
        String email = "test@example.com";
        String password = "Password1";

        when(userService.isUsernameTaken(username)).thenReturn(false);
        when(userService.isEmailTaken(email)).thenReturn(true);

        String result = signupController.registerUser(username, email, password, "Fluffy", "Dog", 5, "Loves to play",
                UserRole.CLIENT,redirectAttributes);

        assertEquals("redirect:/signup", result);
        verify(redirectAttributes).addFlashAttribute("message", "Email is already taken");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        String result = signupController.registerUser("testuser", "test@example.com", "short", "Fluffy", "Dog", 5,
                "Loves to play", UserRole.CLIENT, redirectAttributes);

        assertEquals("redirect:/signup", result);
        verify(redirectAttributes).addFlashAttribute("message",
                "Password must be at least 8 characters long and contain at least one number");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    public void testRegisterUser_InvalidPetAge() {
        String result = signupController.registerUser("testuser", "test@example.com", "Password1", "Fluffy", "Dog", -1,
                 "Loves to play", UserRole.CLIENT, redirectAttributes);

        assertEquals("redirect:/signup", result);
        verify(redirectAttributes).addFlashAttribute("message", "Pet age must be between 0 and 20 years");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }
}
