package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.PrescriptionRequest;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.PrescriptionRequestRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import au.edu.rmit.sept.webapp.model.PrescriptionRequest;
import au.edu.rmit.sept.webapp.repository.PrescriptionRequestRepository;

import java.util.List;

@Service
public class PrescriptionRequestService {
    @Autowired
    private PrescriptionRequestRepository prescriptionRequestRepository;

    public List<PrescriptionRequest> findByUserId(Long userId) {
        return prescriptionRequestRepository.findByUserId(userId);
    }

    public void saveRequest(PrescriptionRequest request) {
        prescriptionRequestRepository.save(request);
    }
}
