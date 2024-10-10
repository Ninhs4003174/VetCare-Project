package au.edu.rmit.sept.webapp.service;

import au.edu.rmit.sept.webapp.model.PrescriptionRequest;
import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.repository.PrescriptionRequestRepository;

import java.util.List;
import java.util.Optional;

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

    public void saveRequest(PrescriptionRequest request) {
        prescriptionRequestRepository.save(request);
    }

    public Optional<PrescriptionRequest> findById(Long id) {
        return prescriptionRequestRepository.findById(id);
    }

    public List<PrescriptionRequest> findAll() {
        return prescriptionRequestRepository.findAll();
    }

    public void deleteById(Long id) {
        prescriptionRequestRepository.deleteById(id);
    }

    public void save(PrescriptionRequest request) {
        prescriptionRequestRepository.save(request);
    }

}
