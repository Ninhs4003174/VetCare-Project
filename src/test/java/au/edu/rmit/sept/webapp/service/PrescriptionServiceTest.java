package au.edu.rmit.sept.webapp.service;


import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.repository.PrescriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @InjectMocks
    private PrescriptionService prescriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePrescription() {
        // Arrange
        Prescription prescription = new Prescription();
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        // Act
        prescriptionService.savePrescription(prescription);

        // Assert
        verify(prescriptionRepository, times(1)).save(prescription);
    }

    @Test
    void testFindPrescriptionsByVetId() {
        // Arrange
        Long vetId = 1L;
        List<Prescription> prescriptions = Arrays.asList(new Prescription(), new Prescription());
        when(prescriptionRepository.findByVetId(vetId)).thenReturn(prescriptions);

        // Act
        List<Prescription> result = prescriptionService.findPrescriptionsByVetId(vetId);

        // Assert
        assertEquals(2, result.size());
        verify(prescriptionRepository, times(1)).findByVetId(vetId);
    }

    @Test
    void testFindByUser() {
        // Arrange
        Long userId = 1L;
        List<Prescription> prescriptions = Arrays.asList(new Prescription(), new Prescription());
        when(prescriptionRepository.findByUserId(userId)).thenReturn(prescriptions);

        // Act
        List<Prescription> result = prescriptionService.findByUser(userId);

        // Assert
        assertEquals(2, result.size());
        verify(prescriptionRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testFindById() {
        // Arrange
        Long id = 1L;
        Prescription prescription = new Prescription();
        when(prescriptionRepository.findById(id)).thenReturn(Optional.of(prescription));

        // Act
        Optional<Prescription> result = prescriptionService.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(prescription, result.get());
        verify(prescriptionRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateDeliveryStatus_WhenPrescriptionExists() {
        // Arrange
        Long prescriptionId = 1L;
        Prescription prescription = new Prescription();
        when(prescriptionRepository.findById(prescriptionId)).thenReturn(Optional.of(prescription));

        // Act
        prescriptionService.updateDeliveryStatus(prescriptionId, "DELIVERED");

        // Assert
        assertEquals("DELIVERED", prescription.getDeliveryStatus());
        assertEquals(LocalDateTime.now().getDayOfMonth(), prescription.getUpdatedAt().getDayOfMonth());
        verify(prescriptionRepository, times(1)).findById(prescriptionId);
        verify(prescriptionRepository, times(1)).save(prescription);
    }

    @Test
    void testUpdateDeliveryStatus_WhenPrescriptionDoesNotExist() {
        // Arrange
        Long prescriptionId = 1L;
        when(prescriptionRepository.findById(prescriptionId)).thenReturn(Optional.empty());

        // Act
        prescriptionService.updateDeliveryStatus(prescriptionId, "DELIVERED");

        // Assert
        verify(prescriptionRepository, times(1)).findById(prescriptionId);
        verify(prescriptionRepository, never()).save(any(Prescription.class));
    }
}

