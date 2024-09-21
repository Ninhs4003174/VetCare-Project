package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.Prescription;
import au.edu.rmit.sept.webapp.model.Vet;
import au.edu.rmit.sept.webapp.repository.PrescriptionRepository;
import au.edu.rmit.sept.webapp.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private VetRepository vetRepository;

    public Prescription requestPrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPendingPrescriptions() {
        return prescriptionRepository.findByRefillStatus("Pending");
    }

    public List<Vet> searchClinics(String query) {
        return vetRepository.findByClinicNameContainingIgnoreCase(query);
    }

    public Vet findVetById(Long vetId) {
        return vetRepository.findById(vetId).orElse(null);
    }
}
