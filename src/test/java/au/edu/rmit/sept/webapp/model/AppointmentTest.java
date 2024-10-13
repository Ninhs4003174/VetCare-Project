package au.edu.rmit.sept.webapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppointmentTest {

    private Appointment appointment;

    @BeforeEach
    void setUp() {
        // Initialize the appointment object
        appointment = new Appointment();
        appointment.setPetName("Max");
        appointment.setDate("2024-10-13");
        appointment.setTime("10:00 AM");
        appointment.setStatus("Confirmed");
    }

    @Test
    void testFormattedDetailsAllFieldsSet() {
        String expectedDetails = "Max on 2024-10-13 at 10:00 AM. Status: Confirmed";
        assertEquals(expectedDetails, appointment.formattedDetails(), "Formatted details should match the expected output when all fields are set");
    }

    @Test
    void testFormattedDetailsWithNullPetName() {
        appointment.setPetName(null);
        String expectedDetails = "Unknown Pet on 2024-10-13 at 10:00 AM. Status: Confirmed";
        assertEquals(expectedDetails, appointment.formattedDetails(), "Formatted details should handle null pet name correctly");
    }

    @Test
    void testFormattedDetailsWithNullDate() {
        appointment.setDate(null);
        String expectedDetails = "Max on Unknown Date at 10:00 AM. Status: Confirmed";
        assertEquals(expectedDetails, appointment.formattedDetails(), "Formatted details should handle null date correctly");
    }

    @Test
    void testFormattedDetailsWithNullTime() {
        appointment.setTime(null);
        String expectedDetails = "Max on 2024-10-13 at Unknown Time. Status: Confirmed";
        assertEquals(expectedDetails, appointment.formattedDetails(), "Formatted details should handle null time correctly");
    }

    @Test
    void testFormattedDetailsWithNullStatus() {
        appointment.setStatus(null);
        String expectedDetails = "Max on 2024-10-13 at 10:00 AM. Status: Unknown Status";
        assertEquals(expectedDetails, appointment.formattedDetails(), "Formatted details should handle null status correctly");
    }

    @Test
    void testFormattedDetailsWithAllNullFields() {
        // Setting all fields to null
        appointment.setPetName(null);
        appointment.setDate(null);
        appointment.setTime(null);
        appointment.setStatus(null);

        String expectedDetails = "Unknown Pet on Unknown Date at Unknown Time. Status: Unknown Status";
        assertEquals(expectedDetails, appointment.formattedDetails(), "Formatted details should handle all null fields correctly");
    }
}
