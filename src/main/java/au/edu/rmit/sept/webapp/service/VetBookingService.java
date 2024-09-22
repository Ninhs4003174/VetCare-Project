package au.edu.rmit.sept.webapp.service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<VetBooking> getFilteredVets(String serviceType, String location) {
    List<VetBooking> allVets = repository.findAll();  // Get all vets from the repository

    return allVets.stream()
        .filter(vet -> (serviceType == null || vet.getServiceType().equalsIgnoreCase(serviceType))
            && (location == null || vet.getClinic().equalsIgnoreCase(location)))
        .collect(Collectors.toList());
}

}
