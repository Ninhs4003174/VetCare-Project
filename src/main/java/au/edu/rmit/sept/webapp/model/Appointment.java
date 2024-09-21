package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String petName;
    private String vetName;
    private String date;
    private String time;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Default no-argument constructor (required by Spring)
    public Appointment() {}

    // New constructor to match the controller's call (without the User parameter)
    public Appointment(Long id, String petName, String vetName, String date, String time, String status) {
        this.id = id;
        this.petName = petName;
        this.vetName = vetName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    // Existing constructor with the User parameter
    public Appointment(Long id, String petName, String vetName, String date, String time, String status, User user) {
        this.id = id;
        this.petName = petName;
        this.vetName = vetName;
        this.date = date;
        this.time = time;
        this.status = status;
        this.user = user;
    }

    // Getters and setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String formattedDetails() {
        // Debugging output
        System.out.println("Vet Name in formattedDetails: " + vetName);

        // Start with pet name
        String appointmentDetails = (petName == null) ? "Unknown Pet" : petName;

        // Add vet name
        appointmentDetails += " with " + ((vetName == null) ? "Unknown Vet" : vetName);

        // Add date and time
        appointmentDetails += " on " + date + " at " + time;

        System.out.println("Formatted Details: " + appointmentDetails);

        return appointmentDetails;
    }
}
