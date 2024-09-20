package au.edu.rmit.sept.webapp.model;

public class Appointment {
    private Long id;
    private String petName;
    private String vetName;
    private String date;
    private String time;
    private String status;

    // Default constructor (required for form binding)
    public Appointment() {}

    public Appointment(Long id, String petName, String vetName, String date, String time, String status) {
        this.id = id;
        this.petName = petName;
        this.vetName = vetName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    // Getters and setters
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

    public String formattedDetails() {
        String appointmentDetails = petName == null ? "Unknown Pet" : petName;
        appointmentDetails += " with " + (vetName == null ? "Unknown Vet" : vetName);
        appointmentDetails += " on " + date + " at " + time;
        return appointmentDetails;
    }
}
