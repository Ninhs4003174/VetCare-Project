package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public void savePrescription(Prescription prescription) {
        prescriptionRepository.save(prescription);
    }

    public List<Prescription> findPrescriptionsByVetId(Long vetId) {
        return prescriptionRepository.findByVetId(vetId);
    }

    public List<Prescription> findByUser(Long userId) {
        return prescriptionRepository.findByUserId(userId);
    }

    public Optional<Prescription> findById(Long id) {
        return prescriptionRepository.findById(id);
    }

    public void updateDeliveryStatus(Long prescriptionId, String newStatus) {
        Optional<Prescription> optionalPrescription = prescriptionRepository.findById(prescriptionId);
        if (optionalPrescription.isPresent()) {
            Prescription prescription = optionalPrescription.get();
            prescription.setDeliveryStatus(newStatus);
            prescription.handleDeliveryStatusChange();
            prescription.setUpdatedAt(LocalDateTime.now());
            prescriptionRepository.save(prescription);
        }
    }
}