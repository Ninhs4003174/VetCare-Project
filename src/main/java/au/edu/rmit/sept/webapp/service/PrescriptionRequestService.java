package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.PrescriptionRequest;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.PrescriptionRequestRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionRequestService {

    private final PrescriptionRequestRepository prescriptionRequestRepository;

    @Autowired
    public PrescriptionRequestService(PrescriptionRequestRepository prescriptionRequestRepository) {
        this.prescriptionRequestRepository = prescriptionRequestRepository;
    }

    public void saveRequest(PrescriptionRequest request) {
        prescriptionRequestRepository.save(request);
    }

    public List<PrescriptionRequest> findRequestsByUser(User user) {
        return prescriptionRequestRepository.findByUser(user);
    }
}
