package au.edu.rmit.sept.webapp.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class AppointmentTest {

    @Test
    void should_formatDetails_when_PetNameAndVetNameAreAvailable() {
        Appointment appointment = new Appointment(1L, "Bella", "Dr. Smith", "2024-09-15", "10:00 AM", "Scheduled");
        assertEquals("Bella with Dr. Smith on 2024-09-15 at 10:00 AM", appointment.formattedDetails());
    }

    @Test
    void should_formatDetails_when_PetNameIsNull() {
        Appointment appointment = new Appointment(1L, null, "Dr. Smith", "2024-09-15", "10:00 AM", "Scheduled");
        assertEquals("Unknown Pet with Dr. Smith on 2024-09-15 at 10:00 AM", appointment.formattedDetails());
    }

    @Test
    void should_formatDetails_when_VetNameIsNull() {
        Appointment appointment = new Appointment(1L, "Bella", null, "2024-09-15", "10:00 AM", "Scheduled");
        assertEquals("Bella with Unknown Vet on 2024-09-15 at 10:00 AM", appointment.formattedDetails());
    }

    @Test
    void should_formatDetails_when_BothPetNameAndVetNameAreNull() {
        Appointment appointment = new Appointment(1L, null, null, "2024-09-15", "10:00 AM", "Scheduled");
        assertEquals("Unknown Pet with Unknown Vet on 2024-09-15 at 10:00 AM", appointment.formattedDetails());
    }
}
