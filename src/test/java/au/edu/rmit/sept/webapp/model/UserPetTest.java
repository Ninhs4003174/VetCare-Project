package au.edu.rmit.sept.webapp.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.HashSet;
import java.util.Set;

public class UserPetTest {

    private User user;
    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    public void setUp() {
        user = new User("john_doe", "password123");
        user.setEmail("john@example.com");
        user.setAddress("123 Main St");
        user.setPhoneNumber("555-1234");

        pet1 = new Pet("Max", "Cat", 2, "Shy cat", user);
        pet2 = new Pet("Luna", "Bird", 1, "Talkative parrot", user);
        user.setPets(new HashSet<>(Set.of(pet1, pet2)));
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" })
    public void testCreateUser() {
        // Simulate user creation (no persistence)
        assertNotNull(user);
        assertEquals("john_doe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("123 Main St", user.getAddress());
        assertEquals("555-1234", user.getPhoneNumber());
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" })
    public void testAddPetToUser() {
        Pet pet = new Pet("Bella", "Dog", 3, "Friendly dog", user);
        user.getPets().add(pet);

        assertEquals(3, user.getPets().size());
        assertTrue(user.getPets().contains(pet));
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" })
    public void testRemovePetFromUser() {
        // Remove one pet
        user.getPets().remove(pet1);

        assertEquals(1, user.getPets().size());
        assertFalse(user.getPets().contains(pet1));
        assertTrue(user.getPets().contains(pet2));
    }
}
