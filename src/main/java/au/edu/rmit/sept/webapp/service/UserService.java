package au.edu.rmit.sept.webapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.repository.PetRepository;
import au.edu.rmit.sept.webapp.repository.UserRepository;
import au.edu.rmit.sept.webapp.repository.PrescriptionRequestRepository;
import au.edu.rmit.sept.webapp.model.Pet;
import au.edu.rmit.sept.webapp.model.PrescriptionRequest;
import au.edu.rmit.sept.webapp.model.User;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PrescriptionRequestRepository prescriptionRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void registerUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Encode the password
        userRepository.save(user);
    }

    public List<Pet> findPetsByUser(User user) {
        return petRepository.findByOwner(user); // You need to implement this in PetRepository
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public List<PrescriptionRequest> findPrescriptionRequestsByUserId(Long userId) {
        return prescriptionRequestRepository.findByUserId(userId); // Ensure this method is implemented in the
                                                                   // repository
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null); // Assuming you have this method
    }

    public void updateUser(User user) {
        userRepository.save(user); // Update user details
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
