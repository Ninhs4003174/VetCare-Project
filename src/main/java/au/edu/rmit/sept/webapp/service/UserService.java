package au.edu.rmit.sept.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.repository.UserRepository;
import au.edu.rmit.sept.webapp.model.User; // Assuming UserEntity is your custom user entity

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Encode the password
        repository.save(user);
    }

    public User findUserByEmail(String email) {
        return repository.findByEmail(email); // Assuming you have this method in your repository
    }

    public boolean isUsernameTaken(String username) {
        return repository.findByUsername(username) != null;
    }

    public boolean isEmailTaken(String email) {
        return repository.findByEmail(email) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username); // Fetch user entity directly
        if (user != null) { // Check if user exists
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
