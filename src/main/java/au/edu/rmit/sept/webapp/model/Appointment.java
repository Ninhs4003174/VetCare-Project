package au.edu.rmit.sept.webapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long petId;
    private String petName;
    private Long vetId;
    private String date;
    private String time;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors, getters, setters

    public Appointment() {
    }

    public Appointment(Long id, Long petId, String petName, Long vetId, String date, String time, String status,
            User user) {
        this.id = id;
        this.petId = petId;
        this.petName = petName;
        this.vetId = vetId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.user = user;
    }

    // Getters and setters for all properties

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Long getVetId() {
        return vetId;
    }

    public void setVetId(Long vetId) {
        this.vetId = vetId;
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

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public String formattedDetails() {
        // Debugging output
        System.out.println("Vet ID in formattedDetails: " + vetId);

        // Start with pet name
        String appointmentDetails = (petName == null) ? "Unknown Pet" : petName;

        // Add vet ID
        appointmentDetails += " with Vet ID " + ((vetId == null) ? "Unknown Vet" : vetId);

        // Add date and time
        appointmentDetails += " on " + date + " at " + time;

        System.out.println("Formatted Details: " + appointmentDetails);

        return appointmentDetails;
    }
}
