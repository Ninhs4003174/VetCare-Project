

package au.edu.rmit.sept.webapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.enums.UserRole;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import au.edu.rmit.sept.webapp.repository.PetRepository;
import au.edu.rmit.sept.webapp.repository.VetBookingRepository;
import au.edu.rmit.sept.webapp.repository.PrescriptionRequestRepository;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private VetBookingRepository vetBookingRepository;

    @Mock
    private PrescriptionRequestRepository prescriptionRequestRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(UserRole.CLIENT);
    }

    @Test
    public void testGetUsersByRole() {
        List<User> users = new ArrayList<>();
        users.add(testUser);
        
        when(userRepository.findByRole(UserRole.CLIENT)).thenReturn(users);

        List<User> result = userService.getUsersByRole(UserRole.CLIENT);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());

        verify(userRepository, times(1)).findByRole(UserRole.CLIENT);
    }

    @Test
    public void testSaveUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        
        userService.saveUser(testUser);

        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void testIsUsernameTaken() {
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        boolean isTaken = userService.isUsernameTaken("testuser");

        assertTrue(isTaken);
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    public void testIsUsernameNotTaken() {
        when(userRepository.findByUsername("newuser")).thenReturn(null);

        boolean isTaken = userService.isUsernameTaken("newuser");

        assertFalse(isTaken);
        verify(userRepository, times(1)).findByUsername("newuser");
    }

    @Test
    public void testFindUserByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        User foundUser = userService.findUserByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    public void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        User result = userService.findById(2L);

        assertNull(result);
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    public void testDeleteUserById() {
        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
