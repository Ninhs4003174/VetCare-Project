package au.edu.rmit.sept.webapp.model;

public class VetBooking {
    private Long id;
    private Long vetUserId;
    private String vetName;
    private String clinic;
    private String serviceType;
    private String phoneNumber; // New field
    private String clinicAddress; // New field
    private String email; // New field
    private Long clinicId; // New field

    // Constructors, getters, and setters
    public VetBooking() {
    }

    public VetBooking(Long id, Long vetUserId, String vetName, String clinic, String serviceType, String phoneNumber,
            String clinicAddress, String email, Long clinicId) {
        this.id = id;
        this.vetUserId = vetUserId;
        this.vetName = vetName;
        this.clinic = clinic;
        this.serviceType = serviceType;
        this.phoneNumber = phoneNumber;
        this.clinicAddress = clinicAddress;
        this.email = email;
        this.clinicId = clinicId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVetUserId() {
        return vetUserId;
    }

    public void setVetUserId(Long vetUserId) {
        this.vetUserId = vetUserId;
    }

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }
}