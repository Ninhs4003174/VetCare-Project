package au.edu.rmit.sept.webapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.edu.rmit.sept.webapp.model.VetBooking;
import au.edu.rmit.sept.webapp.repository.VetBookingRepository;

public class VetBookingServiceTest {

    @InjectMocks
    private VetBookingService vetBookingService;

    @Mock
    private VetBookingRepository vetBookingRepository;

    private List<VetBooking> mockVetBookings;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockVetBookings = new ArrayList<>();

        VetBooking vet1 = new VetBooking();
        vet1.setServiceType("Surgery");
        vet1.setClinic("Melbourne Clinic");

        VetBooking vet2 = new VetBooking();
        vet2.setServiceType("General Checkup");
        vet2.setClinic("Sydney Clinic");

        mockVetBookings.add(vet1);
        mockVetBookings.add(vet2);
    }

    @Test
    public void testGetAllVets() {
        when(vetBookingRepository.findAll()).thenReturn(mockVetBookings);

        List<VetBooking> result = vetBookingService.getAllVets();
        assertEquals(2, result.size());
        assertEquals("Surgery", result.get(0).getServiceType());
        assertEquals("General Checkup", result.get(1).getServiceType());

        verify(vetBookingRepository, times(1)).findAll();
    }

    @Test
    public void testGetFilteredVetsByServiceType() {
        when(vetBookingRepository.findAll()).thenReturn(mockVetBookings);

        List<VetBooking> result = vetBookingService.getFilteredVets("Surgery", null);
        assertEquals(1, result.size());
        assertEquals("Surgery", result.get(0).getServiceType());

        verify(vetBookingRepository, times(1)).findAll();
    }

    @Test
    public void testGetFilteredVetsByLocation() {
        when(vetBookingRepository.findAll()).thenReturn(mockVetBookings);

        List<VetBooking> result = vetBookingService.getFilteredVets(null, "Sydney Clinic");
        assertEquals(1, result.size());
        assertEquals("Sydney Clinic", result.get(0).getClinic());

        verify(vetBookingRepository, times(1)).findAll();
    }

    @Test
    public void testGetFilteredVetsByServiceTypeAndLocation() {
        when(vetBookingRepository.findAll()).thenReturn(mockVetBookings);

        List<VetBooking> result = vetBookingService.getFilteredVets("Surgery", "Melbourne Clinic");
        assertEquals(1, result.size());
        assertEquals("Surgery", result.get(0).getServiceType());
        assertEquals("Melbourne Clinic", result.get(0).getClinic());

        verify(vetBookingRepository, times(1)).findAll();
    }

    @Test
    public void testGetFilteredVetsNoMatch() {
        when(vetBookingRepository.findAll()).thenReturn(mockVetBookings);

        List<VetBooking> result = vetBookingService.getFilteredVets("Dentistry", "Brisbane Clinic");
        assertEquals(0, result.size());

        verify(vetBookingRepository, times(1)).findAll();
    }
}
