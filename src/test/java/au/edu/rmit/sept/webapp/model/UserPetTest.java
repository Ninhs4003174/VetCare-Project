package au.edu.rmit.sept.webapp.model;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class UserPetTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test-persistence-unit");
    }

    @BeforeEach
    public void setUpTest() {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
    }

    @AfterAll
    public static void tearDown() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testCreateUser() {
        User user = new User("john_doe", "password123");
        user.setEmail("john@example.com");
        user.setAddress("123 Main St");
        user.setPhoneNumber("555-1234");

        entityManager.persist(user);
        entityManager.getTransaction().commit();

        User foundUser = entityManager.find(User.class, user.getId());

        assertNotNull(foundUser);
        assertEquals("john_doe", foundUser.getUsername());
        assertEquals("john@example.com", foundUser.getEmail());
        assertEquals("123 Main St", foundUser.getAddress());
        assertEquals("555-1234", foundUser.getPhoneNumber());
    }

    @Test
    public void testAddPetToUser() {
        User user = new User("jane_doe", "password456");
        entityManager.persist(user);

        Pet pet = new Pet("Bella", "Dog", 3, "Friendly dog", user);
        user.getPets().add(pet);

        entityManager.persist(pet);
        entityManager.getTransaction().commit();

        User foundUser = entityManager.find(User.class, user.getId());
        assertNotNull(foundUser);
        assertEquals(1, foundUser.getPets().size());

        Pet foundPet = foundUser.getPets().iterator().next();
        assertEquals("Bella", foundPet.getName());
        assertEquals("Dog", foundPet.getType());
        assertEquals(3, foundPet.getAge());
        assertEquals("Friendly dog", foundPet.getBio());
        assertEquals(user, foundPet.getOwner());
    }

    @Test
    public void testRemovePetFromUser() {
        User user = new User("mike_smith", "password789");
        Pet pet1 = new Pet("Max", "Cat", 2, "Shy cat", user);
        Pet pet2 = new Pet("Luna", "Bird", 1, "Talkative parrot", user);

        user.setPets(new HashSet<>(Set.of(pet1, pet2)));

        entityManager.persist(user);
        entityManager.persist(pet1);
        entityManager.persist(pet2);
        entityManager.getTransaction().commit();

        // Remove one pet
        entityManager.getTransaction().begin();
        user.getPets().remove(pet1);
        entityManager.getTransaction().commit();

        User foundUser = entityManager.find(User.class, user.getId());
        assertEquals(1, foundUser.getPets().size());
        assertFalse(foundUser.getPets().contains(pet1));
        assertTrue(foundUser.getPets().contains(pet2));
    }
}
