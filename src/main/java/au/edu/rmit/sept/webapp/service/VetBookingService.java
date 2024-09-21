package au.edu.rmit.sept.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.model.VetBooking;
import au.edu.rmit.sept.webapp.repository.VetBookingRepository;

@Service
public class VetBookingService {

    private final VetBookingRepository repository;

    @Autowired
    public VetBookingService(VetBookingRepository repository) {
        this.repository = repository;
    }

    public List<VetBooking> getAllVets() {
        return repository.findAll();
    }
}
