package au.edu.rmit.sept.webapp.model;

public class VetBooking {
    private Long id;
    private String name;
    private String clinic;
    private String serviceType; 
    // Constructors, getters, and setters
    public VetBooking() {}

    public VetBooking(Long id, String name, String clinic,String serviceType) {
        this.id = id;
        this.name = name;
        this.clinic = clinic;
        this.serviceType = serviceType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
