package au.edu.rmit.sept.webapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.model.User;
import au.edu.rmit.sept.webapp.model.VetBooking;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import au.edu.rmit.sept.webapp.repository.VetBookingRepository;

@Service
public class VetBookingService {

    private final VetBookingRepository repository;
     private final UserRepository userRepository;

    @Autowired
    public VetBookingService(VetBookingRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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
     private String getClinicName(Long vetUserId) {
      User vetUser = userRepository.findById(vetUserId).orElse(null);
    if (vetUser != null) {
        // Here the username of the vet user will represent the clinic name
        return vetUser.getUsername();  // Assuming clinic name is stored in the 'username'
    }
    return "Unknown Clinic";

}
}