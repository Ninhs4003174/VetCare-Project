package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescription_requests")
public class PrescriptionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    private String petName; // Use petName instead of pet relationship
    private String status = "PENDING"; // Possible values: PENDING, APPROVED, DENIED
    private LocalDateTime requestedAt = LocalDateTime.now();
    private LocalDateTime processedAt;
    private String comments;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private User processedBy;

    // Constructors, getters, setters
    public PrescriptionRequest() {
    }

    public PrescriptionRequest(Prescription prescription, String petName, String comments) {
        this.prescription = prescription;
        this.petName = petName;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public User getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(User processedBy) {
        this.processedBy = processedBy;
        this.processedAt = LocalDateTime.now();
        this.status = "APPROVED"; // Assuming the status should be set to APPROVED when processed
    }

    public Long getPrescriptionId() {
        return this.prescription != null ? this.prescription.getId() : null;
    }

    public void setPrescriptionId(Long prescriptionId) {
        if (this.prescription == null) {
            this.prescription = new Prescription();
        }
        this.prescription.setId(prescriptionId);
    }
}