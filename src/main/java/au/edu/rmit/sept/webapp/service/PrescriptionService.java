package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.repository.PrescriptionRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}