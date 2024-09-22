package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @ManyToOne
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    private String dosage;

    @Column(length = 500)
    private String instructions;

    @Temporal(TemporalType.DATE)
    private Date issueDate = new Date();

    @Column(length = 20)
    private String refillStatus = "Pending"; // Default value

    // Constructors, getters, setters
    public Prescription() {
    }

    public Prescription(Long petId, Vet vet, String medicationName, String dosage, String instructions) {
        this.petId = petId;
        this.vet = vet;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.instructions = instructions;
    }

    // Getters and setters
}
