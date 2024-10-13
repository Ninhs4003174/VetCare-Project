package au.edu.rmit.sept.webapp.controller;

import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.PrescriptionRequest;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.service.PetService;
import au.edu.rmit.sept.webapp.service.PrescriptionRequestService;
import au.edu.rmit.sept.webapp.service.PrescriptionService;
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

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileControllerTests {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private UserService userService;

    @Mock
    private PrescriptionRequestService prescriptionRequestService;

    @Mock
    private PrescriptionService prescriptionService;

    @Mock
    private PetService petService;

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

    @Test
    public void testShowAddPetForm_UserFound() {
        when(userService.findByUsername("testUser")).thenReturn(user);

        String viewName = profileController.showAddPetForm(model);

        assertEquals("add-pet", viewName);
        verify(model).addAttribute("user", user);
    }

    @Test
    public void testShowAddPetForm_UserNotFound() {
        when(userService.findByUsername("testUser")).thenReturn(null);

        String viewName = profileController.showAddPetForm(model);

        assertEquals("error", viewName);
        verify(model).addAttribute("message", "User not found");
    }

    @Test
    public void testAddPet_UserFound() {
        when(userService.findByUsername("testUser")).thenReturn(user);

        String result = profileController.addPet("Buddy", "Dog", 5, "Friendly dog", redirectAttributes);

        assertEquals("redirect:/profile", result);
        verify(petService).addPet(any(Pet.class));
        verify(redirectAttributes).addFlashAttribute("message", "Pet added successfully!");
        verify(redirectAttributes).addFlashAttribute("success", true);
    }

    @Test
    public void testAddPet_UserNotFound() {
        when(userService.findByUsername("testUser")).thenReturn(null);

        String result = profileController.addPet("Buddy", "Dog", 5, "Friendly dog", redirectAttributes);

        assertEquals("redirect:/add-pet", result);
        verify(redirectAttributes).addFlashAttribute("message", "User not found");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    public void testAddPet_InvalidPetAge() {
        when(userService.findByUsername("testUser")).thenReturn(user);

        String result = profileController.addPet("Buddy", "Dog", 25, "Friendly dog", redirectAttributes);

        assertEquals("redirect:/add-pet", result);
        verify(redirectAttributes).addFlashAttribute("message", "Pet age must be between 0 and 20 years");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    public void testShowUserPrescriptions_UserFound() {
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(prescriptionService.findByUser(1L)).thenReturn(Collections.emptyList());

        String viewName = profileController.showUserPrescriptions(model);

        assertEquals("profile/user-prescriptions", viewName);
        verify(model).addAttribute("prescriptions", Collections.emptyList());
    }

    @Test
    public void testShowUserPrescriptions_UserNotFound() {
        when(userService.findByUsername("testUser")).thenReturn(null);

        String viewName = profileController.showUserPrescriptions(model);

        assertEquals("error", viewName);
        verify(model).addAttribute("message", "User not found");
    }

    @Test
    public void testShowRequestPrescriptionForm_UserFound() {
        Prescription prescription = new Prescription();
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(prescriptionService.findById(1L)).thenReturn(java.util.Optional.of(prescription));

        String viewName = profileController.showRequestPrescriptionForm(1L, model);

        assertEquals("profile/request-prescription", viewName);
        verify(model).addAttribute("prescription", prescription);
    }

    @Test
    public void testShowRequestPrescriptionForm_UserNotFound() {
        when(userService.findByUsername("testUser")).thenReturn(null);

        String viewName = profileController.showRequestPrescriptionForm(1L, model);

        assertEquals("error", viewName);
        verify(model).addAttribute("message", "User not found");
    }

    @Test
    public void testShowRequestPrescriptionForm_PrescriptionNotFound() {
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(prescriptionService.findById(1L)).thenReturn(java.util.Optional.empty());

        String viewName = profileController.showRequestPrescriptionForm(1L, model);

        assertEquals("error", viewName);
        verify(model).addAttribute("message", "Prescription not found");
    }

    @Test
    public void testRequestPrescription_UserFound() {
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        prescription.setPetName("Buddy");
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(prescriptionService.findById(1L)).thenReturn(java.util.Optional.of(prescription));

        String result = profileController.requestPrescription(1L, redirectAttributes);

        assertEquals("redirect:/user-prescriptions", result);
        verify(prescriptionRequestService).save(any(PrescriptionRequest.class));
        verify(redirectAttributes).addFlashAttribute("message", "Prescription request submitted successfully!");
        verify(redirectAttributes).addFlashAttribute("success", true);
    }

    @Test
    public void testRequestPrescription_UserNotFound() {
        when(userService.findByUsername("testUser")).thenReturn(null);

        String result = profileController.requestPrescription(1L, redirectAttributes);

        assertEquals("redirect:/user-prescriptions", result);
        verify(redirectAttributes).addFlashAttribute("message", "User not found");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    public void testRequestPrescription_PrescriptionNotFound() {
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(prescriptionService.findById(1L)).thenReturn(java.util.Optional.empty());

        String result = profileController.requestPrescription(1L, redirectAttributes);

        assertEquals("redirect:/user-prescriptions", result);
        verify(redirectAttributes).addFlashAttribute("message", "Prescription not found");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    public void testShowEditPetForm_PetFound() {
        Pet pet = new Pet();
        when(petService.findById(1L)).thenReturn(pet);

        String viewName = profileController.showEditPetForm(1L, model);

        assertEquals("edit-pet", viewName);
        verify(model).addAttribute("pet", pet);
    }

    @Test
    public void testShowEditPetForm_PetNotFound() {
        when(petService.findById(1L)).thenReturn(null);

        String viewName = profileController.showEditPetForm(1L, model);

        assertEquals("error", viewName);
        verify(model).addAttribute("message", "Pet not found");
    }

    @Test
    public void testEditPet_PetFound() {
        Pet pet = new Pet();
        when(petService.findById(1L)).thenReturn(pet);

        String result = profileController.editPet(1L, "Buddy", "Dog", 5, "Friendly dog", redirectAttributes);

        assertEquals("redirect:/profile", result);
        verify(petService).updatePet(pet);
        assertEquals("Buddy", pet.getName());
        assertEquals("Dog", pet.getType());
        assertEquals(5, pet.getAge());
        assertEquals("Friendly dog", pet.getBio());
        verify(redirectAttributes).addFlashAttribute("message", "Pet details updated successfully!");
        verify(redirectAttributes).addFlashAttribute("success", true);
    }

    @Test
    public void testEditPet_PetNotFound() {
        when(petService.findById(1L)).thenReturn(null);

        String result = profileController.editPet(1L, "Buddy", "Dog", 5, "Friendly dog", redirectAttributes);

        assertEquals("redirect:/edit-pet/1", result);
        verify(redirectAttributes).addFlashAttribute("message", "Pet not found");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }

    @Test
    public void testEditPet_InvalidPetAge() {
        Pet pet = new Pet();
        when(petService.findById(1L)).thenReturn(pet);

        String result = profileController.editPet(1L, "Buddy", "Dog", 25, "Friendly dog", redirectAttributes);

        assertEquals("redirect:/edit-pet/1", result);
        verify(redirectAttributes).addFlashAttribute("message", "Pet age must be between 0 and 20 years");
        verify(redirectAttributes).addFlashAttribute("success", false);
    }
}