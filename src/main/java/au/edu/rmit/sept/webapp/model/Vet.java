package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vet")
public class Vet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vetId;

    private String clinicName;
    private String address;
    private String phoneNumber;
    private String email;

    // Getters and Setters
}
