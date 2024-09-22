package au.edu.rmit.sept.webapp.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PetRecordTest {

    private PetRecord petRecord;

    @BeforeEach
    void setUp() {
        // Create a new PetRecord object before each test
        petRecord = new PetRecord();
    }

    @Test
    void testSettersAndGetters() {
        // Use setters to set values
        petRecord.setName("Buddy");
        petRecord.setBreed("Labrador");
        petRecord.setDateOfBirth("2020-10-20");
        petRecord.setVeterinarian("Dr. White");

        // Check that the getters return the expected values
        assertEquals("Buddy", petRecord.getName());
        assertEquals("Labrador", petRecord.getBreed());
        assertEquals("2020-10-20", petRecord.getDateOfBirth());
        assertEquals("Dr. White", petRecord.getVeterinarian());
    }

    @Test
    void testConstructorWithParameters() {
        // Use the constructor to create a new PetRecord object
        PetRecord newPetRecord = new PetRecord("Fluffy", "Golden Retriever", "2015-06-01", null, null, null, null, null,
                null, null, null, "Dr. Smith");

        // Assert that the fields are correctly initialized
        assertEquals("Fluffy", newPetRecord.getName());
        assertEquals("Golden Retriever", newPetRecord.getBreed());
        assertEquals("2015-06-01", newPetRecord.getDateOfBirth());
        assertEquals("Dr. Smith", newPetRecord.getVeterinarian());
    }

    @Test
    void testSettersForNullValues() {
        // Set some values to null and verify the behavior
        petRecord.setName(null);
        petRecord.setBreed(null);
        petRecord.setDateOfBirth(null);
        petRecord.setVeterinarian(null);

        // Check that the values are correctly set to null
        assertNull(petRecord.getName());
        assertNull(petRecord.getBreed());
        assertNull(petRecord.getDateOfBirth());
        assertNull(petRecord.getVeterinarian());
    }
}
